package ru.akonyaev.camunda.demo.process.speakAtMeetup

import mu.KLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import ru.akonyaev.camunda.demo.component.PresentationStorage
import ru.akonyaev.camunda.demo.process.fileId
import java.util.concurrent.TimeUnit

@Component
class ShareVideoDelegate(
    private val presentationStorage: PresentationStorage
) : JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        logger.info { "Sharing video..." }
        TimeUnit.SECONDS.sleep(3)

        presentationStorage.share(execution.fileId)
    }

    companion object : KLogging()
}
