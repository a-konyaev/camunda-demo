package ru.akonyaev.camunda.demo.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KLogging
import org.camunda.bpm.engine.HistoryService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.history.HistoricProcessInstance
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.akonyaev.camunda.demo.model.SpeakAtMeetupProcessInfo
import ru.akonyaev.camunda.demo.model.SpeakAtMeetupRequest
import ru.akonyaev.camunda.demo.process.ProcessDefinitionName
import java.time.ZoneId
import java.util.Date
import javax.validation.Valid

@Tag(name = "Speak at a meetup (Camunda platform)")
@RestController
@RequestMapping("/process")
class ProcessController(
    private val runtimeService: RuntimeService,
    private val historyService: HistoryService
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
        val activeProcesses = getActiveProcessInstances(request.requestId)
        return when (activeProcesses.size) {
            0 -> {
                val id = runtimeService.startProcessInstanceByKey(
                    ProcessDefinitionName.SpeakAtMeetupProcess.name,
                    request.requestId,
                    request.toProcessVariablesMap()
                ).id
                logger.info {
                    "New process instance with id = $id started on request: $request"
                }
                id
            }
            1 -> {
                val id = activeProcesses.first().id
                logger.warn {
                    "Process instance with the same requestId = ${request.requestId} " +
                        "and correlationId = ${request.correlationId} already exists: id = $id"
                }
                id
            }
            else -> throw IllegalStateException(
                "There are several process instances with the same " +
                    "applicationId = ${request.requestId} " +
                    "and correlationId = ${request.correlationId} already exists!"
            )
        }
    }

    @Operation(summary = "Get active processes")
    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getActiveProcesses() =
        this.getActiveProcessInstances()
            .map {
                SpeakAtMeetupProcessInfo(
                    instanceId = it.id,
                    state = it.state,
                    startTime = it.startTime.toLocalDateTime()
                )
            }

    private fun Date.toLocalDateTime() =
        this.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

    private fun getActiveProcessInstances(requestId: String? = null): List<HistoricProcessInstance> {
        val query = historyService
            .createHistoricProcessInstanceQuery()
            .processDefinitionKey(ProcessDefinitionName.SpeakAtMeetupProcess.name)

        requestId?.let {
            query.processInstanceBusinessKey(it)
        }

        return query.active().list()
    }

    companion object : KLogging()
}
