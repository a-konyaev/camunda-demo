package ru.akonyaev.camunda.demo.configuration

import io.camunda.zeebe.client.api.JsonMapper
import io.camunda.zeebe.spring.client.EnableZeebeClient
import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.akonyaev.camunda.demo.component.CustomZeebeJsonMapper

@Configuration
@EnableZeebeClient
class ZeebeConfiguration {

    @Bean
    fun jsonMapper(): JsonMapper {
        return CustomZeebeJsonMapper()
    }
}

// @ZeebeDeployment has been extracted into a separate configuration due to recursive dependency on JsonMapper-bean:
// if @ZeebeDeployment is placed in ZeebeConfiguration, then Spring failed on startup
@Configuration
@ZeebeDeployment(resources = ["classpath*:/zeebe/**/*.bpmn"])
class ZeebeDeploymentConfiguration
