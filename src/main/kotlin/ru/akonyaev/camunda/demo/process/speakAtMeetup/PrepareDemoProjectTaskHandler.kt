package ru.akonyaev.camunda.demo.process.speakAtMeetup

import mu.KLogging
import org.springframework.stereotype.Component
import ru.akonyaev.camunda.demo.task.TaskHandlerNoInputOutput
import java.util.concurrent.TimeUnit

@Component
class PrepareDemoProjectTaskHandler : TaskHandlerNoInputOutput() {

    override fun handleNoInputOutput() {
        logger.info { "Preparing demo project..." }
        TimeUnit.SECONDS.sleep(1)
    }

    companion object : KLogging()
}
