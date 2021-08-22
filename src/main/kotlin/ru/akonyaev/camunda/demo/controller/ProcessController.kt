package ru.akonyaev.camunda.demo.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import mu.KLogging
import org.camunda.bpm.engine.HistoryService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.history.HistoricProcessInstance
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.akonyaev.camunda.demo.process.ProcessDefinitionName
import javax.validation.Valid

@Api(
    description = "Выступление на митапе",
    tags = ["speak-at-meetup"]
)
@RestController
@RequestMapping("/process")
class ProcessController(
    private val runtimeService: RuntimeService,
    private val historyService: HistoryService
) {

    @ApiOperation(value = "Запустить процесс")
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Process started", response = String::class),
            ApiResponse(code = 400, message = "Bad request", response = ResponseStatus::class),
            ApiResponse(code = 500, message = "Internal error", response = ResponseStatus::class)
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

    @ApiOperation(value = "Получить список активных процессов")
    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getActiveProcesses() =
        this.getActiveProcessInstances()
            .map {
                SpeakAtMeetupProcessInfo(
                    instanceId = it.id,
                    state = it.state,
                    startTime = it.startTime
                )
            }

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
