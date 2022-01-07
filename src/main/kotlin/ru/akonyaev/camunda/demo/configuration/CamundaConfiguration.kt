package ru.akonyaev.camunda.demo.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import ru.akonyaev.camunda.demo.configuration.postprocessors.TaskDelegateRegistrar

@Configuration
class CamundaConfiguration {

    @Bean
    fun taskDelegateRegistrar(appContext: GenericApplicationContext) = TaskDelegateRegistrar(appContext)
}
