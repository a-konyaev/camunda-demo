package ru.akonyaev.camunda.demo.process

import org.camunda.bpm.engine.delegate.DelegateExecution
import ru.akonyaev.camunda.demo.camunda.VariableDelegate
import java.time.LocalDateTime

const val REQUEST_ID = "requestId"
var DelegateExecution.requestId: String by VariableDelegate(REQUEST_ID)

const val TITLE = "title"
var DelegateExecution.title: String by VariableDelegate(TITLE)

const val TEMPLATE_ID = "templateId"
var DelegateExecution.templateId: String by VariableDelegate(TEMPLATE_ID)

const val CORRELATION_ID = "correlationId"
var DelegateExecution.correlationId: String by VariableDelegate(CORRELATION_ID)

const val PRESENTATION_DATE = "presentationDate"
var DelegateExecution.presentationDate: LocalDateTime by VariableDelegate(PRESENTATION_DATE)

const val REVIEW_ITERATION = "reviewIteration"
var DelegateExecution.reviewIteration: Int by VariableDelegate(REVIEW_ITERATION)

const val HAS_REMARKS = "hasRemarks"
var DelegateExecution.hasRemarks: Boolean by VariableDelegate(HAS_REMARKS)

const val FILE_ID = "fileId"
var DelegateExecution.fileId: String by VariableDelegate(FILE_ID)
