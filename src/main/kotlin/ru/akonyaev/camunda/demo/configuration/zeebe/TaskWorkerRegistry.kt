package ru.akonyaev.camunda.demo.configuration.zeebe

import io.camunda.zeebe.spring.client.ZeebeClientLifecycle
import ru.akonyaev.camunda.demo.task.TaskHandler
import ru.akonyaev.camunda.demo.task.TaskWorker

class TaskWorkerRegistry(
    clientLifecycle: ZeebeClientLifecycle,
    taskHandlers: List<TaskHandler<*, *>>
) : AutoCloseable {

    private val taskWorkers: List<TaskWorker> = taskHandlers.map { TaskWorker(it) }

    init {
        clientLifecycle.addStartListener { client ->
            taskWorkers.forEach { it.open(client) }
        }
    }

    override fun close() {
        taskWorkers.forEach { it.close() }
    }
}
