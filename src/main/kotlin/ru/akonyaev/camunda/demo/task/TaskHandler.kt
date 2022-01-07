package ru.akonyaev.camunda.demo.task

import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

abstract class TaskHandler<Input : Any, Output : Any>(
    private val inputClass: KClass<Input>,
    private val outputClass: KClass<Output>
) {
    private val inputVariables: List<TaskHandlerVariable> = getVariablesByClass(inputClass)

    // TODO: not used?
    private val outputVariables: List<TaskHandlerVariable> = getVariablesByClass(outputClass)

    private fun getVariablesByClass(clazz: KClass<*>): List<TaskHandlerVariable> {
        if (clazz == EmptyInputOutput::class) {
            return listOf()
        }
        if (!clazz.isData) {
            throw IllegalArgumentException("TaskHandler Input and Output class must be data class")
        }
        val variables = clazz.declaredMemberProperties.map {
            TaskHandlerVariable(
                name = it.name,
                clazz = it.returnType.classifier as KClass<*>
            )
        }
        if (variables.isEmpty()) {
            throw IllegalArgumentException("Cannot get variables by input/output class: $clazz")
        }
        return variables
    }

    @Suppress("UNCHECKED_CAST")
    fun handleRaw(variableGetter: (String) -> Any?, variableSetter: (String, Any?) -> Unit) {
        // TODO: separate to several functions to avoid if-s
        val input = if (inputClass == EmptyInputOutput::class) {
            EmptyInputOutput as Input
        } else {
            val inputVariables = inputVariables.associate { v ->
                v.name to variableGetter(v.name)?.let { v.clazz.cast(it) }
            }
            packVariablesToInput(inputVariables)
        }
        val output = handle(input) ?: return

        if (outputClass != EmptyInputOutput::class) {
            val outputVariables = unpackOutputToVariables(output)
            outputVariables.forEach { (name, value) -> variableSetter(name, value) }
        }
    }

    abstract fun handle(input: Input): Output?

    private fun packVariablesToInput(variables: Map<String, Any?>): Input {
        val constructor = inputClass.primaryConstructor!!
        val args = constructor.parameters.associateWith { variables[it.name] }
        return constructor.callBy(args)
    }

    private fun unpackOutputToVariables(output: Output): Map<String, Any?> {
        return outputClass.declaredMemberProperties.associate { it.name to it.get(output) }
    }

    private data class TaskHandlerVariable(
        val name: String,
        val clazz: KClass<*>
    )
}
