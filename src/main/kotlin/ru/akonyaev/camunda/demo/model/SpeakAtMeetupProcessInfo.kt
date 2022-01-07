package ru.akonyaev.camunda.demo.model

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

@Schema
data class SpeakAtMeetupProcessInfo(
    @Schema(name = "Process instance id", required = true)
    @field:NotBlank
    val instanceId: String,

    @Schema(name = "Process instance status", required = true)
    @field:NotBlank
    val state: String,

    @Schema(name = "Process start date and time", required = true)
    @field:NotBlank
    val startTime: LocalDateTime
)
