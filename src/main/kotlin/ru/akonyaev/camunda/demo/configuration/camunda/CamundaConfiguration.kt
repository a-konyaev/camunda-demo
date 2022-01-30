package ru.akonyaev.camunda.demo.configuration.camunda

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext

@Configuration
class CamundaConfiguration {

    @Bean
    fun taskDelegateRegistrar(appContext: GenericApplicationContext) = TaskDelegateRegistrar(appContext)
}
