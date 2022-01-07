package ru.akonyaev.camunda.demo.process.speakAtMeetup

import mu.KLogging
import org.springframework.stereotype.Component
import ru.akonyaev.camunda.demo.task.TaskHandlerNoInput
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Component
class AgreeAnAppointmentTaskHandler :
    TaskHandlerNoInput<AgreeAnAppointmentTaskHandler.Output>(Output::class) {

    override fun handleNoInput(): Output? {
        logger.info { "Agree an appointment..." }
        TimeUnit.SECONDS.sleep(1)

        return Output(
            presentationDate = LocalDateTime.now().plusDays(30)
        )
    }

    data class Output(
        val presentationDate: LocalDateTime
    )

    companion object : KLogging()
}
