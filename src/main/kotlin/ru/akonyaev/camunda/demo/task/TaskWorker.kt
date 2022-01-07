package ru.akonyaev.camunda.demo.task

import io.camunda.zeebe.client.ZeebeClient
import io.camunda.zeebe.client.api.command.FinalCommandStep
import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.client.api.worker.JobHandler
import io.camunda.zeebe.client.api.worker.JobWorker
import ru.akonyaev.camunda.demo.utils.camelToKebabCase
import ru.akonyaev.camunda.demo.utils.getLogicName
import ru.akonyaev.camunda.demo.utils.getWorkerName
import ru.akonyaev.camunda.demo.utils.ifPositive
import ru.akonyaev.camunda.demo.utils.ifPositiveAsDurationInMillis
import java.util.concurrent.atomic.AtomicReference

class TaskWorker(
    private val taskHandler: TaskHandler<*, *>,
    private val properties: Properties = defaultProperties
) : JobHandler, AutoCloseable {

    private val jobWorkerHolder: AtomicReference<JobWorker> = AtomicReference()

    fun open(client: ZeebeClient) {
        val builder = client
            .newWorker()
            .jobType(properties.jobType ?: taskHandler.getLogicName().camelToKebabCase())
            .handler(this)

        with(properties) {
            builder.name(name ?: taskHandler.getWorkerName())
            maxJobsActive.ifPositive { builder.maxJobsActive(it) }
            timeout.ifPositiveAsDurationInMillis { builder.timeout(it) }
            pollInterval.ifPositiveAsDurationInMillis { builder.pollInterval(it) }
            requestTimeout.ifPositiveAsDurationInMillis { builder.requestTimeout(it) }
            fetchVariables.takeIf { it.isNotEmpty() }?.let { builder.fetchVariables(it) }
        }

        val worker = builder.open()
        jobWorkerHolder.set(worker)
    }

    override fun close() {
        jobWorkerHolder.get()?.close()
    }

    override fun handle(jobClient: JobClient, job: ActivatedJob) {
        val inputVariables = job.variablesAsMap
        val outputVariables = mutableMapOf<String, Any?>()
        try {
            taskHandler.handleRaw(
                variableGetter = { name -> inputVariables[name] },
                variableSetter = { name, value -> outputVariables[name] = value }
            )
            sendCompleteCommand(jobClient, job, outputVariables)
        } catch (error: TaskBpmnError) {
            sendErrorCommand(jobClient, job, error)
        }
    }

    private fun sendErrorCommand(jobClient: JobClient, job: ActivatedJob, error: TaskBpmnError) {
        val command = jobClient
            .newThrowErrorCommand(job.key)
            .errorCode(error.errorCode)
            .errorMessage(error.errorMessage)
        sendCommand(job, command)
    }

    private fun sendCompleteCommand(
        jobClient: JobClient,
        job: ActivatedJob,
        variables: Map<String, Any?>
    ) {
        val command = jobClient
            .newCompleteCommand(job.key)
            .variables(variables)
        sendCommand(job, command)
    }

    private fun sendCommand(job: ActivatedJob, command: FinalCommandStep<*>) {
        command
            .send()
            .exceptionally { handleCommandError(job, command, it) }
    }

    private fun handleCommandError(
        job: ActivatedJob,
        command: FinalCommandStep<*>,
        throwable: Throwable
    ): Nothing {
        throw RuntimeException(
            "Could not send [$command] for job [$job] to Zeebe due to error: ${throwable.message}",
            throwable
        )
    }

    data class Properties(
        val name: String? = null,
        val jobType: String? = null,
        val maxJobsActive: Int = 0,
        val timeout: Long = 0,
        val pollInterval: Long = 0,
        val requestTimeout: Long = 0,
        val fetchVariables: List<String> = listOf()
    )

    companion object {
        private val defaultProperties = Properties()
    }
}
