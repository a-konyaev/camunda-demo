package ru.akonyaev.camunda.demo.camunda

import org.camunda.bpm.engine.delegate.DelegateExecution
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class VariableDelegate<T>(
    private val variableName: String? = null,
    private val defaultValue: T? = null
) : ReadWriteProperty<DelegateExecution, T> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: DelegateExecution, property: KProperty<*>): T =
        thisRef.getVariable(variableName(property)) as T ?: defaultValue as T

    override fun setValue(thisRef: DelegateExecution, property: KProperty<*>, value: T) {
        thisRef.setVariable(variableName(property), value)
    }

    private fun variableName(property: KProperty<*>) = variableName ?: property.name
}
