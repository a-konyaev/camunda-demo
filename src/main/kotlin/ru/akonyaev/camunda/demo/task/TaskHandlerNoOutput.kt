package ru.akonyaev.camunda.demo.task

import kotlin.reflect.KClass

abstract class TaskHandlerNoOutput<Input : Any>(inputClass: KClass<Input>) :
    TaskHandler<Input, EmptyInputOutput>(inputClass, EmptyInputOutput::class) {

    override fun handle(input: Input): EmptyInputOutput {
        handleNoOutput(input)
        return EmptyInputOutput
    }

    abstract fun handleNoOutput(input: Input)
}
