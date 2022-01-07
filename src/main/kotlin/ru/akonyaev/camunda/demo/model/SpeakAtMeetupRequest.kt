package ru.akonyaev.camunda.demo.model

import io.swagger.v3.oas.annotations.media.Schema
import ru.akonyaev.camunda.demo.camunda.CORRELATION_ID
import ru.akonyaev.camunda.demo.camunda.REQUEST_ID
import ru.akonyaev.camunda.demo.camunda.TITLE
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema
data class SpeakAtMeetupRequest(
    @Schema(name = "Request identity", required = true)
    @field:NotBlank
    val requestId: String,

    @Schema(name = "Meetup title", required = true)
    @field:NotNull
    val title: String,

    @Schema(name = "Id to correlate a request with a response", required = true)
    @field:NotBlank
    val correlationId: String
) {
    fun toProcessVariablesMap() = mapOf(
        REQUEST_ID to requestId,
        TITLE to title,
        CORRELATION_ID to correlationId
    )
}
