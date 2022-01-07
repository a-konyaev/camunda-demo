package ru.akonyaev.camunda.demo.process.speakAtMeetup

import mu.KLogging
import org.springframework.stereotype.Component
import ru.akonyaev.camunda.demo.component.PresentationStorage
import ru.akonyaev.camunda.demo.task.TaskHandlerNoOutput
import java.util.concurrent.TimeUnit

@Component
class ShareVideoTaskHandler(
    private val presentationStorage: PresentationStorage
) : TaskHandlerNoOutput<ShareVideoTaskHandler.Input>(Input::class) {

    override fun handleNoOutput(input: Input) {
        logger.info { "Sharing video..." }
        TimeUnit.SECONDS.sleep(1)

        presentationStorage.share(input.fileId)
    }

    data class Input(
        val fileId: String
    )

    companion object : KLogging()
}
