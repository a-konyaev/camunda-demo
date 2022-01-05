package ru.akonyaev.camunda.demo.utils

import java.time.Duration

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
