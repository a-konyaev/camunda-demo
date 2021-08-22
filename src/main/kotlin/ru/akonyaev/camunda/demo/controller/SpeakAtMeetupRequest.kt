package ru.akonyaev.camunda.demo.controller

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import ru.akonyaev.camunda.demo.process.CORRELATION_ID
import ru.akonyaev.camunda.demo.process.PRESENTATION_DATE
import ru.akonyaev.camunda.demo.process.REQUEST_ID
import ru.akonyaev.camunda.demo.process.TITLE
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel(
    description = "Запрос на выступление на митапе"
)
data class SpeakAtMeetupRequest(
    @ApiModelProperty("Идентификатор запроса", required = true)
    @field:NotBlank
    val requestId: String,

    @ApiModelProperty("Название", required = true)
    @field:NotNull
    val title: String,

    @ApiModelProperty("Идентификатор для корреляции запроса и ответа", required = true)
    @field:NotBlank
    val correlationId: String
) {
    fun toProcessVariablesMap() = mapOf(
        REQUEST_ID to requestId,
        TITLE to title,
        CORRELATION_ID to correlationId
    )
}
