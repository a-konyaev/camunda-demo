package ru.akonyaev.camunda.demo.workflow

class TaskBpmnError(
    val errorCode: String,
    val errorMessage: String
) : RuntimeException("[$errorCode] $errorMessage")
