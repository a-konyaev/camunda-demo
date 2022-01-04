package ru.akonyaev.camunda.demo.process.preparePresentation

import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.spring.client.annotation.ZeebeVariable
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker
import mu.KLogging
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class FixRemarksWorker() {

    @ZeebeWorker(
        name = "FixRemarksWorker",
        type = "fix-remarks",
        autoComplete = true
    )
    fun handle(
        client: JobClient, job: ActivatedJob,
        @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
        @ZeebeVariable reviewIteration: Integer,
        @ZeebeVariable templateId: String,
    ): Map<String, Any> {
        logger.info { "Fixing remarks..." }
        TimeUnit.SECONDS.sleep(3)

        val iter = reviewIteration.toInt()
        return mapOf(
            "hasRemarks" to (iter < 2),
            "reviewIteration" to (iter + 1)
        )
    }

    companion object : KLogging()
}
