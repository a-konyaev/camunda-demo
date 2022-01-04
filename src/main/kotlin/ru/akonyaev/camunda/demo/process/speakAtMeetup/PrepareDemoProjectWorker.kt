package ru.akonyaev.camunda.demo.process.speakAtMeetup

import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker
import mu.KLogging
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class PrepareDemoProjectWorker {

    @ZeebeWorker(
        name = "PrepareDemoProjectWorker",
        type = "prepare-demo-project",
        autoComplete = true
    )
    fun handle(client: JobClient, job: ActivatedJob): Map<String, Any> {
        logger.info { "Preparing demo project..." }
        TimeUnit.SECONDS.sleep(3)
        return mapOf()
    }

    companion object : KLogging()
}
