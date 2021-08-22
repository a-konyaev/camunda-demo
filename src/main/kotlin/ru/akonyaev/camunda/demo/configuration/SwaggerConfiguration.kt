package ru.akonyaev.camunda.demo.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.akonyaev.camunda.demo.CamundaDemoApplication
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.UiConfiguration
import springfox.documentation.swagger.web.UiConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.Collections

@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    @Bean
    fun api(): Docket =
        Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(
                RequestHandlerSelectors.basePackage(
                    CamundaDemoApplication::class.java.getPackage().name
                )
            )
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
            .useDefaultResponseMessages(true)

    @Bean
    fun uiConfiguration(): UiConfiguration =
        UiConfigurationBuilder.builder()
            .displayRequestDuration(true)
            .validatorUrl("")
            .build()

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
            "",
            "",
            "",
            "",
            null,
            "",
            "",
            Collections.emptyList()
        )
    }
}
