package ru.akonyaev.camunda.demo.controller

import io.camunda.zeebe.spring.client.ZeebeClientLifecycle
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import mu.KLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.akonyaev.camunda.demo.model.SpeakAtMeetupProcessInfo
import ru.akonyaev.camunda.demo.model.SpeakAtMeetupRequest
import ru.akonyaev.camunda.demo.process.ProcessDefinitionName
import java.util.concurrent.TimeUnit
import javax.validation.Valid

@Api(
    description = "Speak at a meetup (for Zeebe)",
    tags = ["speak-at-meetup"]
)
@RestController
@RequestMapping("/process/zeebe")
class ZeebeProcessController(
    private val client: ZeebeClientLifecycle
) {

    @ApiOperation(value = "Start the process")
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Process started", response = String::class),
            ApiResponse(code = 400, message = "Bad request", response = ResponseStatus::class),
            ApiResponse(code = 500, message = "Internal error", response = ResponseStatus::class)
        ]
    )
    @PostMapping("/start", produces = [MediaType.TEXT_PLAIN_VALUE])
    fun startProcess(@Valid @RequestBody request: SpeakAtMeetupRequest): String {
        ensureClientStarted()

        val id = client
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

    @ApiOperation(value = "Get active processes")
    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getActiveProcesses(): List<SpeakAtMeetupProcessInfo> {
        ensureClientStarted()

        // TODO: Spring Zeebe client cannot get process instances info

        return listOf()
    }

    private fun ensureClientStarted() {
        if (!client.isRunning) {
            throw IllegalStateException("Zeebe client is not started")
        }
    }

    companion object : KLogging()
}
