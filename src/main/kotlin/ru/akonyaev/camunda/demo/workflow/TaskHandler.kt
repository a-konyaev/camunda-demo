package ru.akonyaev.camunda.demo.workflow

import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

abstract class TaskHandler<Input : Any, Output : Any>(
    private val inputClass: KClass<Input>,
    private val outputClass: KClass<Output>
) {
    private val inputVariables: List<TaskHandlerVariable> = getClassFields(inputClass)
    private val outputVariables: List<TaskHandlerVariable> = getClassFields(outputClass)

    private fun getClassFields(clazz: KClass<*>): List<TaskHandlerVariable> {
        if (!clazz.isData) {
            throw IllegalArgumentException("TaskHandler Input and Output class must be data class")
        }
        return clazz.declaredMemberProperties.map {
            TaskHandlerVariable(
                name = it.name,
                clazz = it.returnType.classifier as KClass<*>
            )
        }
    }

    fun handle(variableGetter: (String) -> Any?, variableSetter: (String, Any?) -> Unit) {
        val inputVariables = inputVariables.associate { v ->
            v.name to variableGetter(v.name)?.let { v.clazz.cast(it) }
        }
        val input = packVariablesToInput(inputVariables)

        val output = handle(input)

        val outputVariables = unpackOutputToVariables(output)
        outputVariables.forEach { (name, value) -> variableSetter(name, value) }
    }

    protected abstract fun handle(input: Input): Output

    private fun packVariablesToInput(variables: Map<String, Any?>): Input {
        val constructor = inputClass.primaryConstructor!!
        val args = constructor.parameters.associateWith { variables[it.name] }
        return constructor.callBy(args)
    }

    private fun unpackOutputToVariables(output: Output): Map<String, Any?> {
        return outputClass.declaredMemberProperties.associate { it.name to it.get(output) }
    }
}

data class TaskHandlerVariable(
    val name: String,
    val clazz: KClass<*>
)
