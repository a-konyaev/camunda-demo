package ru.akonyaev.camunda.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CamundaDemoApplication

fun main(args: Array<String>) {
    runApplication<CamundaDemoApplication>(*args)
}
