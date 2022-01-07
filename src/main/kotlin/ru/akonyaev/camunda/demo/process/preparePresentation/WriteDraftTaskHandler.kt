package ru.akonyaev.camunda.demo.process.preparePresentation

import mu.KLogging
import org.springframework.stereotype.Component
import ru.akonyaev.camunda.demo.component.PresentationStorage
import ru.akonyaev.camunda.demo.task.TaskHandler
import java.util.concurrent.TimeUnit

@Component
class WriteDraftTaskHandler(
    private val presentationStorage: PresentationStorage
) : TaskHandler<WriteDraftTaskHandler.Input, WriteDraftTaskHandler.Output>(Input::class, Output::class) {

    override fun handle(input: Input): Output {
        logger.info { "Writing draft with title '${input.title}' and template '${input.templateId}'" }
        TimeUnit.SECONDS.sleep(1)
        val fileId = presentationStorage.create(input.title, input.templateId)

        return Output(
            reviewIteration = 0,
            fileId = fileId
        )
    }

    data class Input(
        val title: String,
        val templateId: String
    )

    data class Output(
        val reviewIteration: Int,
        val fileId: String
    )

    companion object : KLogging()
}
