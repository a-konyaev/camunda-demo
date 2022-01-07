package ru.akonyaev.camunda.demo.utils

import org.camunda.bpm.engine.delegate.JavaDelegate
import ru.akonyaev.camunda.demo.task.TaskDelegate
import ru.akonyaev.camunda.demo.task.TaskHandler

fun TaskHandler<*, *>.getLogicName() =
    this::class.getShortName()
        .lowercaseFirstChar()
        .removeSuffix("TaskHandler")

fun TaskHandler<*, *>.getDelegateName() = this.getLogicName() + "Delegate"

fun TaskHandler<*, *>.getWorkerName() = this.getLogicName() + "Worker"

fun TaskHandler<*, *>.toDelegate() = TaskDelegate(this)

fun JavaDelegate.getName() =
    when (this) {
        is TaskDelegate -> this.taskHandler.getDelegateName()
        else -> this.javaClass.simpleName.lowercaseFirstChar()
    }
