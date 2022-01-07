package ru.akonyaev.camunda.demo.task

class TaskBpmnError(
    val errorCode: String,
    val errorMessage: String
) : RuntimeException("[$errorCode] $errorMessage")
