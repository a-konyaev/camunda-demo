package ru.akonyaev.camunda.demo.process.preparePresentation

import io.camunda.zeebe.spring.client.ZeebeClientLifecycle
import org.springframework.stereotype.Component
import ru.akonyaev.camunda.demo.workflow.TaskWorker
import ru.akonyaev.camunda.demo.workflow.TaskWorkerProperties

@Component
class FixRemarksWorker(
    taskHandler: FixRemarksTaskHandler,
    clientLifecycle: ZeebeClientLifecycle
) : TaskWorker(
    TaskWorkerProperties(
        name = "FixRemarksWorker",
        jobType = "fix-remarks"
    ),
    taskHandler,
    clientLifecycle
) {

//    @ZeebeWorker(
//        name = "FixRemarksWorker",
//        type = "fix-remarks",
//        autoComplete = true
//    )
//    fun handle(
//        client: JobClient, job: ActivatedJob,
//        @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
//        @ZeebeVariable reviewIteration: Integer,
//    ): Map<String, Any> {
//        val input = FixRemarksTaskHandler.Input(
//            reviewIteration = reviewIteration.toInt()
//        )
//        val output = taskHandler.handle(input)
//
//        return mapOf(
//            "hasRemarks" to output.hasRemarks,
//            "reviewIteration" to output.reviewIteration
//        )
//    }
}
