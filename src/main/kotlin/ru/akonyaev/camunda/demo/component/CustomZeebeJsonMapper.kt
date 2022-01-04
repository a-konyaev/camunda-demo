package ru.akonyaev.camunda.demo.component

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.camunda.zeebe.client.api.JsonMapper
import io.camunda.zeebe.client.api.command.InternalClientException
import java.io.IOException
import java.io.InputStream
import java.util.TimeZone

class CustomZeebeJsonMapper(
    private val objectMapper: ObjectMapper = defaultObjectMapper()
) : JsonMapper {

    override fun <T> fromJson(json: String, typeClass: Class<T>): T {
        return try {
            objectMapper.readValue(json, typeClass)
        } catch (e: IOException) {
            throw InternalClientException(
                "Failed to deserialize json '$json' to class '$typeClass'", e
            )
        }
    }

    override fun fromJsonAsMap(json: String): Map<String, Any> {
        return try {
            objectMapper.readValue(json, MAP_TYPE_REFERENCE)
        } catch (e: IOException) {
            throw InternalClientException(
                "Failed to deserialize json '$json' to 'Map<String, Object>'", e
            )
        }
    }

    override fun fromJsonAsStringMap(json: String): Map<String, String> {
        return try {
            objectMapper.readValue(json, STRING_MAP_TYPE_REFERENCE)
        } catch (e: IOException) {
            throw InternalClientException(
                "Failed to deserialize json '$json' to 'Map<String, String>'", e
            )
        }
    }

    override fun toJson(value: Any): String {
        return try {
            objectMapper.writeValueAsString(value)
        } catch (e: JsonProcessingException) {
            throw InternalClientException(
                "Failed to serialize object '$value' to json", e
            )
        }
    }

    override fun validateJson(propertyName: String?, jsonInput: String?): String? {
        return try {
            objectMapper.readTree(jsonInput).toString()
        } catch (e: IOException) {
            throw InternalClientException(
                "Failed to validate json input '$jsonInput' for property '$propertyName'", e
            )
        }
    }

    override fun validateJson(propertyName: String?, jsonInput: InputStream?): String? {
        return try {
            objectMapper.readTree(jsonInput).toString()
        } catch (e: IOException) {
            throw InternalClientException(
                "Failed to validate json input stream for property '$propertyName'", e
            )
        }
    }

    companion object {
        private val MAP_TYPE_REFERENCE: TypeReference<Map<String, Any>> =
            object : TypeReference<Map<String, Any>>() {}

        private val STRING_MAP_TYPE_REFERENCE: TypeReference<Map<String, String>> =
            object : TypeReference<Map<String, String>>() {}

        private fun defaultObjectMapper() =
            ObjectMapper()
                .findAndRegisterModules()
//                .registerModule(JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setTimeZone(TimeZone.getDefault())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }
}
