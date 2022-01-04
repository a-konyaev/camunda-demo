package ru.akonyaev.camunda.demo.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import ru.akonyaev.camunda.demo.process.CORRELATION_ID
import ru.akonyaev.camunda.demo.process.REQUEST_ID
import ru.akonyaev.camunda.demo.process.TITLE
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel
data class SpeakAtMeetupRequest(
    @ApiModelProperty("Request identity", required = true)
    @field:NotBlank
    val requestId: String,

    @ApiModelProperty("Meetup title", required = true)
    @field:NotNull
    val title: String,

    @ApiModelProperty("Id to correlate a request with a response", required = true)
    @field:NotBlank
    val correlationId: String
) {
    fun toProcessVariablesMap() = mapOf(
        REQUEST_ID to requestId,
        TITLE to title,
        CORRELATION_ID to correlationId
    )
}