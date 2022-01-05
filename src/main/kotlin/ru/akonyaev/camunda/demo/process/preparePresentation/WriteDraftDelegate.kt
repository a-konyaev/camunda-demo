package ru.akonyaev.camunda.demo.process.preparePresentation

import mu.KLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import ru.akonyaev.camunda.demo.component.PresentationStorage
import ru.akonyaev.camunda.demo.camunda.fileId
import ru.akonyaev.camunda.demo.camunda.reviewIteration
import ru.akonyaev.camunda.demo.camunda.templateId
import ru.akonyaev.camunda.demo.camunda.title
import java.util.concurrent.TimeUnit

@Component
class WriteDraftDelegate(
    private val presentationStorage: PresentationStorage
) : JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        logger.info { "Writing draft with title '${execution.title}' and template '${execution.templateId}'" }
        TimeUnit.SECONDS.sleep(3)

        execution.reviewIteration = 0
        execution.fileId = presentationStorage.create(execution.title, execution.templateId)
    }

    companion object : KLogging()
}
