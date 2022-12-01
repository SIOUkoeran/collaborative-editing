package com.example.websocket_server

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ObjectStringConverter(
    private val objectMapper: ObjectMapper,
) {
    private val log = KotlinLogging.logger { }

    fun <T> stringToObject(data: String?, clazz: Class<T>): Mono<T> {
        return Mono.fromCallable { objectMapper.readValue(data, clazz) }
            .doOnError {
                log.error("Error converting [{$data}] to class : ${clazz.simpleName}")
            }
    }

    fun <T> objectToString(`object`: T): Mono<String> {
        return Mono.fromCallable { objectMapper.writeValueAsString(`object`) }
            .doOnError { log.error("Error converting $`object` to String") }
    }
}