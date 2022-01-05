package ru.akonyaev.camunda.demo.process.speakAtMeetup

import mu.KLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import ru.akonyaev.camunda.demo.camunda.presentationDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Component
class AgreeAnAppointmentDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        logger.info { "Agree an appointment..." }
        TimeUnit.SECONDS.sleep(3)

        execution.presentationDate = LocalDateTime.now().plusDays(30)
    }

    companion object : KLogging()
}
