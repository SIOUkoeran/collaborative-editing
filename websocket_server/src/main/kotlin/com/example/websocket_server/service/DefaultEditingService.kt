package com.example.websocket_server.service

import com.example.websocket_server.*
import com.example.websocket_server.model.CreateDocument
import com.example.websocket_server.model.DocumentDto
import com.example.websocket_server.model.UpdateDocument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactive.collect
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono


@Component
@InternalCoroutinesApi
class DefaultEditingService(

) : EditingService {

    private val log = KotlinLogging.logger { }

    /**
     * send message through web socket
     */
    override suspend fun sendDocumentMessage(documentDto: DocumentDto): Mono<Void> = mono {

    }.then()

    override suspend fun sendCreateDocumentMessage(documentDto: CreateDocument): Mono<Void> = coroutineScope {
        log.info {
            "create new document userId : ${documentDto.userId[0]} document id : ${documentDto.documentId} "
        }
        sendEvent(
            documentDto.userId[0],
            CreateFileEvent(
                docId = documentDto.documentId,
                userId = documentDto.userId[0]
            )
        )
    }.then()

    override suspend fun sendUpdateDocumentMessage(documentDto: UpdateDocument): Mono<Void> = mono {
        documentDto.userId.stream()
            .toFlux()
            .log()
            .collect { user ->
                sendEvent(
                    user,
                    NewChangeEvent(
                        docId = documentDto.documentId,
                        payload = documentDto
                    )
                )
            }
    }.then()

    private suspend fun sendEvent(userId: Long, webSocketEvent: WebSocketEvent): Mono<Void> = coroutineScope {
        Mono.fromCallable {
            SinkWrapper.sinks
                .emitNext(
                    SendTo(
                        userId,
                        webSocketEvent
                    ), Sinks.EmitFailureHandler.FAIL_FAST
                )
        }.subscribeOn(Schedulers.boundedElastic()).then()
    }
}