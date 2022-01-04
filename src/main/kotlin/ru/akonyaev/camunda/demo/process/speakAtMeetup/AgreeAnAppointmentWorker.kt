package ru.akonyaev.camunda.demo.process.speakAtMeetup

import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker
import mu.KLogging
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Component
class AgreeAnAppointmentWorker {

    @ZeebeWorker(
        name = "AgreeAnAppointmentWorker",
        type = "agree-an-appointment",
        autoComplete = true
    )
    fun handle(client: JobClient, job: ActivatedJob): Map<String, Any> {
        logger.info { "Agree an appointment..." }
        TimeUnit.SECONDS.sleep(3)

        return mapOf("presentationDate" to LocalDateTime.now().plusDays(30))
    }

    companion object : KLogging()
}
