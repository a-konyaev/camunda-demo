package ru.akonyaev.camunda.demo.process.preparePresentation

import org.springframework.stereotype.Component
import ru.akonyaev.camunda.demo.workflow.TaskDelegate

@Component
class FixRemarksDelegate(taskHandler: FixRemarksTaskHandler) : TaskDelegate(taskHandler) {

//    override fun execute(execution: DelegateExecution) {
//        val input = FixRemarksTaskHandler.Input(
//            reviewIteration = execution.reviewIteration
//        )
//        val output = taskHandler.handle(input)
//
//        execution.hasRemarks = output.hasRemarks
//        execution.reviewIteration = output.reviewIteration
//    }
}
