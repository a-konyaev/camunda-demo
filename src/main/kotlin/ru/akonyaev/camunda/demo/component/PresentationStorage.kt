package ru.akonyaev.camunda.demo.component

import mu.KLogging
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PresentationStorage {

    fun create(title: String, templateId: String): String {
        val fileId = UUID.randomUUID().toString()
        logger.info { "Create new presentation with title '$title' and template '$templateId': file id = $fileId" }
        return fileId
    }

    fun share(fileId: String) {
        logger.info { "Share presentation: file id = $fileId" }
    }

    companion object : KLogging()
}
