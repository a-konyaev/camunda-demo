package ru.akonyaev.camunda.demo.process.preparePresentation

import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.spring.client.annotation.ZeebeVariable
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker
import mu.KLogging
import org.springframework.stereotype.Component
import ru.akonyaev.camunda.demo.component.PresentationStorage
import java.util.concurrent.TimeUnit

@Component
class WriteDraftWorker(
    private val presentationStorage: PresentationStorage
) {

    @ZeebeWorker(
        name = "WriteDraftWorker",
        type = "write-draft",
        autoComplete = true
    )
    fun handle(
        client: JobClient, job: ActivatedJob,
        @ZeebeVariable title: String,
        @ZeebeVariable templateId: String,
    ): Map<String, Any> {
        logger.info { "Writing draft with title '$title' and template '$templateId'" }
        TimeUnit.SECONDS.sleep(3)

        val fileId = presentationStorage.create(title, templateId)

        return mapOf(
            "reviewIteration" to 0,
            "fileId" to fileId
        )
    }

    companion object : KLogging()
}
