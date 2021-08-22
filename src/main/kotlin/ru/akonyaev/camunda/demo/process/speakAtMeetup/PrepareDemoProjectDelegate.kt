package ru.akonyaev.camunda.demo.process.speakAtMeetup

import mu.KLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class PrepareDemoProjectDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        logger.info { "Preparing demo project..." }
        TimeUnit.SECONDS.sleep(3)
    }

    companion object : KLogging()
}
