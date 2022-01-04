package ru.akonyaev.camunda.demo.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

@ApiModel
data class SpeakAtMeetupProcessInfo(
    @ApiModelProperty("Process instance id", required = true)
    @field:NotBlank
    val instanceId: String,

    @ApiModelProperty("Process instance status", required = true)
    @field:NotBlank
    val state: String,

    @ApiModelProperty("Process start date and time", required = true)
    @field:NotBlank
    val startTime: LocalDateTime
)
