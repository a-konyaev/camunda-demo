package ru.akonyaev.camunda.demo.camunda

import org.camunda.bpm.engine.delegate.DelegateExecution
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class EnumVariableDelegate<T : Enum<T>>(
    private val enumClass: KClass<T>,
    variable: String? = null
) {
    private val internalVarDelegate = VariableDelegate<String>(variable)

    operator fun getValue(execution: DelegateExecution, property: KProperty<*>) =
        internalVarDelegate.getValue(execution, property).primitiveToEnum(enumClass)

    operator fun setValue(execution: DelegateExecution, property: KProperty<*>, value: T) {
        internalVarDelegate.setValue(execution, property, value.toPrimitive())
    }

    companion object Converter {
        fun Enum<*>.toPrimitive() = this.name

        fun <T : Enum<T>> Any.primitiveToEnum(enumClass: KClass<T>): T =
            when (this) {
                is String -> java.lang.Enum.valueOf(enumClass.java, this) as T
                else -> throw IllegalStateException(
                    "The primitive value of the Enum variable is not a string: $this"
                )
            }
    }
}
