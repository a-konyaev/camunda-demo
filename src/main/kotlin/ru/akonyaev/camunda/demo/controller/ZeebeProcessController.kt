package ru.akonyaev.camunda.demo.controller

import io.camunda.zeebe.spring.client.ZeebeClientLifecycle
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.akonyaev.camunda.demo.model.SpeakAtMeetupProcessInfo
import ru.akonyaev.camunda.demo.model.SpeakAtMeetupRequest
import ru.akonyaev.camunda.demo.process.ProcessDefinitionName
import java.util.concurrent.TimeUnit
import javax.validation.Valid

@Tag(name = "Speak at a meetup (Zeebe)")
@RestController
@ConditionalOnProperty("zeebe.enabled")
@RequestMapping("/process/zeebe")
class ZeebeProcessController(
    private val clientLifecycle: ZeebeClientLifecycle
) {

    @Operation(summary = "Start the process")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Process started"),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal error")
        ]
    )
    @PostMapping("/start", produces = [MediaType.TEXT_PLAIN_VALUE])
    fun startProcess(@Valid @RequestBody request: SpeakAtMeetupRequest): String {
        ensureClientStarted()

        val id = clientLifecycle
            .newCreateInstanceCommand()
            .bpmnProcessId(ProcessDefinitionName.SpeakAtMeetupProcess.name)
            .latestVersion()
            .variables(request.toProcessVariablesMap())
            .send()
            .join(10, TimeUnit.SECONDS)
            .processInstanceKey
        logger.info { "New process instance with key = $id started on request: $request" }

        return id.toString()
    }

    @Operation(summary = "Get active processes")
    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getActiveProcesses(): List<SpeakAtMeetupProcessInfo> {
        ensureClientStarted()

        // TODO: Spring Zeebe client cannot get process instances info

        return listOf()
    }

    private fun ensureClientStarted() {
        if (!clientLifecycle.isRunning) {
            throw IllegalStateException("Zeebe client is not started")
        }
    }

    companion object : KLogging()
}
