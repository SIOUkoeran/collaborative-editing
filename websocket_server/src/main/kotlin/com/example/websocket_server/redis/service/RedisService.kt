package com.example.websocket_server.redis.service

import com.example.websocket_server.ObjectStringConverter
import com.example.websocket_server.model.CreateDocument
import com.example.websocket_server.model.DocumentDto
import com.example.websocket_server.model.UpdateDocument
import com.example.websocket_server.service.EditingService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.collect
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.listenToChannelAsFlow
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import reactor.core.Disposable
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.error

@Service
class RedisService(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>,
    private val objectStringConverter: ObjectStringConverter,
    private val editingService: EditingService,
) {
    private val log = KotlinLogging.logger {}

    /**
     * redis pub
     */
    fun publish(topic: String, message: String): Mono<Void> {
        log.info("publish message : $message to $topic")
        return reactiveRedisTemplate.convertAndSend(topic, message).then()
    }

    /**
     * redis sub 분기점
     */
    suspend fun subscribeDocument() = coroutineScope {
        reactiveRedisTemplate.listenToChannelAsFlow("document/**")
            .map { it.message }
            .asFlux()
            .flatMap { objectStringConverter.stringToObject(it, DocumentDto::class.java) }
            .collect { message ->
                when (message) {
                    is UpdateDocument -> {
                        editingService.sendUpdateDocumentMessage(message)
                    }
                    is CreateDocument -> {
                        editingService.sendCreateDocumentMessage(message)
                    }
                    else -> error<Void?>(RuntimeException()).subscribe()
                }
            }
    }

    @EventListener(ApplicationStartedEvent::class)
    @Async
    suspend fun subscribe() {
        subscribeDocument()
    }
}