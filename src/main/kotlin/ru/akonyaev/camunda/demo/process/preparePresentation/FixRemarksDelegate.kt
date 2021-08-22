package ru.akonyaev.camunda.demo.process.preparePresentation

import mu.KLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import ru.akonyaev.camunda.demo.process.hasRemarks
import ru.akonyaev.camunda.demo.process.reviewIteration
import java.util.concurrent.TimeUnit

@Component
class FixRemarksDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        logger.info { "Fixing remarks..." }
        TimeUnit.SECONDS.sleep(3)

        val iter = execution.reviewIteration
        execution.hasRemarks = (iter < 2)
        execution.reviewIteration = iter + 1
    }

    companion object : KLogging()
}
