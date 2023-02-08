package co.mvpmatch.backendtask1.helper

import co.mvpmatch.backendtask1.config.ResourceAlreadyExistsException
import co.mvpmatch.backendtask1.config.ResourceNotFoundException
import co.mvpmatch.backendtask1.config.UnauthorizedException
import co.mvpmatch.backendtask1.config.UserNotAllowedException
import co.mvpmatch.backendtask1.web.api.model.ProductModifyPayload
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.security.access.AccessDeniedException
import kotlin.reflect.typeOf

class GeneralHelper {
    companion object {
        fun throwBestException(e: Exception) {
            when (e.cause) {
                is AccessDeniedException -> throw AccessDeniedException(e.localizedMessage)
                is ResourceAlreadyExistsException -> throw ResourceAlreadyExistsException()
                is ResourceNotFoundException -> throw ResourceNotFoundException()
                is UserNotAllowedException -> throw UserNotAllowedException()
                is UnauthorizedException -> throw UnauthorizedException()
            }
            throw e
        }

        fun serializeJson(payload: Any): String {
            val mapper = ObjectMapper()
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
            val ow: ObjectWriter = mapper.writer().withDefaultPrettyPrinter()
            return ow.writeValueAsString(payload)
        }

        inline fun <reified T> deserializeJson(payload: String): T {
            val mapper = ObjectMapper()
            return mapper.reader().forType(T::class.java).readValue(payload)
        }
    }
}
