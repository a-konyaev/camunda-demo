package ru.akonyaev.camunda.demo.process.preparePresentation

import mu.KLogging
import org.springframework.stereotype.Component
import ru.akonyaev.camunda.demo.task.TaskHandler
import java.util.concurrent.TimeUnit

@Component
class FixRemarksTaskHandler :
    TaskHandler<FixRemarksTaskHandler.Input, FixRemarksTaskHandler.Output>(Input::class, Output::class) {

    override fun handle(input: Input): Output {
        logger.info { "Fixing remarks..." }
        TimeUnit.SECONDS.sleep(1)

        return Output(
            hasRemarks = input.reviewIteration < 2,
            reviewIteration = input.reviewIteration + 1
        )
    }

    data class Input(
        val reviewIteration: Int
    )

    data class Output(
        val hasRemarks: Boolean,
        val reviewIteration: Int
    )

    companion object : KLogging()
}

