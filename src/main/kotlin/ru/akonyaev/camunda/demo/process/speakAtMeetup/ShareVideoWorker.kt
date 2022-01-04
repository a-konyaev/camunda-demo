package ru.akonyaev.camunda.demo.process.speakAtMeetup

import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.spring.client.annotation.ZeebeVariable
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker
import mu.KLogging
import org.springframework.stereotype.Component
import ru.akonyaev.camunda.demo.component.PresentationStorage
import java.util.concurrent.TimeUnit

@Component
class ShareVideoWorker(
    private val presentationStorage: PresentationStorage
) {

    @ZeebeWorker(
        name = "ShareVideoWorker",
        type = "share-video",
        autoComplete = true
    )
    fun handle(
        client: JobClient, job: ActivatedJob,
        @ZeebeVariable fileId: String?
    ): Map<String, Any> {
        logger.info { "Sharing video..." }

        fileId?.let {
            TimeUnit.SECONDS.sleep(3)
            presentationStorage.share(fileId)
            logger.info { "Sharing video: done" }
        } ?: run {
            logger.info { "Sharing video: fileId is null!" }
        }

        return mapOf()
    }

    companion object : KLogging()
}
