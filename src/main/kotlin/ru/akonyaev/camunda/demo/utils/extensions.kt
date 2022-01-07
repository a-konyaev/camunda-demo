package ru.akonyaev.camunda.demo.utils

import java.time.Duration
import java.util.Locale
import kotlin.reflect.KClass

fun String.lowercaseFirstChar() = replaceFirstChar { it.lowercase(Locale.getDefault()) }

fun String.camelToKebabCase(): String {
    val d = 'a' - 'A'
    return fold(StringBuilder(length)) { acc, c ->
        if (c in 'A'..'Z') {
            if (acc.isNotEmpty()) acc.append('-')
            acc.append(c + d)
        } else {
            acc.append(c)
        }
    }.toString()
}

inline fun Int.ifPositive(block: (Int) -> Unit) {
    if (this > 0) block(this)
}

inline fun Long.ifPositiveAsDurationInMillis(block: (Duration) -> Unit) {
    if (this > 0) block(Duration.ofMillis(this))
}

fun KClass<*>.getShortName() =
    this.simpleName ?: throw IllegalStateException("Cannot get simple name for class: $this")
