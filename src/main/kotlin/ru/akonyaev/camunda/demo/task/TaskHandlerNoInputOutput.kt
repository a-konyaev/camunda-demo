package ru.akonyaev.camunda.demo.task

abstract class TaskHandlerNoInputOutput :
    TaskHandler<EmptyInputOutput, EmptyInputOutput>(EmptyInputOutput::class, EmptyInputOutput::class) {

    override fun handle(input: EmptyInputOutput): EmptyInputOutput {
        handleNoInputOutput()
        return EmptyInputOutput
    }

    abstract fun handleNoInputOutput()
}
