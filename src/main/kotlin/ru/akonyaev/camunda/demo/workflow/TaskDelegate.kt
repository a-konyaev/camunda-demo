package ru.akonyaev.camunda.demo.workflow

import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

abstract class TaskDelegate(
    private val taskHandler: TaskHandler<*, *>
) : JavaDelegate {

    override fun execute(execution: DelegateExecution) {
        try {
            taskHandler.handle(
                variableGetter = { name -> execution.getVariable(name) },
                variableSetter = { name, value -> execution.setVariable(name, value) }
            )
        } catch (error: TaskBpmnError) {
            throw BpmnError(error.errorCode, error.errorMessage)
        }
    }
}
