package ru.akonyaev.camunda.demo.task

import kotlin.reflect.KClass

abstract class TaskHandlerNoInput<Output : Any>(outputClass: KClass<Output>) :
    TaskHandler<EmptyInputOutput, Output>(EmptyInputOutput::class, outputClass) {

    override fun handle(input: EmptyInputOutput) = handleNoInput()

    abstract fun handleNoInput(): Output?
}
