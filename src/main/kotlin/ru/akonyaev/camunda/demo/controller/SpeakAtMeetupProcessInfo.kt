package ru.akonyaev.camunda.demo.controller

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.Date
import javax.validation.constraints.NotBlank

@ApiModel(
    description = "Информация по процессу выступления на митапе"
)
data class SpeakAtMeetupProcessInfo(
    @ApiModelProperty("Идентификатор экземпляра процесса", required = true)
    @field:NotBlank
    val instanceId: String,

    @ApiModelProperty("Статус процесса", required = true)
    @field:NotBlank
    val state: String,

    @ApiModelProperty("Дата и время запуска процесса", required = true)
    @field:NotBlank
    val startTime: Date
)
