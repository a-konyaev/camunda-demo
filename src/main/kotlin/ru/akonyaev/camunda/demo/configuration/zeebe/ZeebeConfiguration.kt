package ru.akonyaev.camunda.demo.configuration.zeebe

import io.camunda.zeebe.client.api.JsonMapper
import io.camunda.zeebe.spring.client.EnableZeebeClient
import io.camunda.zeebe.spring.client.ZeebeClientLifecycle
import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment
import io.camunda.zeebe.spring.client.properties.ZeebeClientConfigurationProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.akonyaev.camunda.demo.task.TaskHandler

@Configuration
@ConditionalOnProperty("zeebe.enabled")
@EnableZeebeClient
@EnableConfigurationProperties(ZeebeClientConfigurationProperties::class)
class ZeebeConfiguration {

    @Bean
    fun jsonMapper(): JsonMapper {
        return CustomZeebeJsonMapper()
    }

    @Bean
    fun taskWorkerRegistry(
        clientLifecycle: ZeebeClientLifecycle,
        taskHandlers: List<TaskHandler<*, *>>
    ) = TaskWorkerRegistry(clientLifecycle, taskHandlers)
}

// @ZeebeDeployment has been extracted into a separate configuration due to recursive dependency on JsonMapper-bean:
// if @ZeebeDeployment is placed in ZeebeConfiguration, then Spring failed on startup
@Configuration
@ConditionalOnProperty("zeebe.enabled")
@ZeebeDeployment(resources = ["classpath*:/zeebe/**/*.bpmn"])
class ZeebeDeploymentConfiguration

