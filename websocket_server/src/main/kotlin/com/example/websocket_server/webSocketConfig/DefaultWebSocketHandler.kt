package com.example.websocket_server.webSocketConfig

import com.example.websocket_server.*
import com.example.websocket_server.domain.User
import com.example.websocket_server.login.jwtutils.CoroutineCacheManager
import com.example.websocket_server.login.jwtutils.JWTProperties
import com.example.websocket_server.login.jwtutils.JWTUtils
import com.example.websocket_server.redis.service.RedisService
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.ConcurrentHashMap


@Component
class DefaultWebSocketHandler(
    private val objectMapper: ObjectMapper,
    private val redisService: RedisService,
    private val objectStringConverter: ObjectStringConverter,
    private val jwtProperties: JWTProperties
) : WebSocketHandler {

    private val logger = KotlinLogging.logger { }
    private val userIdToSession: MutableMap<Long, LinkedList<WebSocketSession>> =
        ConcurrentHashMap()

    override fun handle(session: WebSocketSession): Mono<Void> = mono {
        val jwt = session.attributes["jwt"].toString()

        val decode = JWTUtils.decode(
            token = jwt,
            issuer = jwtProperties.issuer,
            secret = jwtProperties.secret,
        )
        logger.info("from userId : ${decode.id} handle websocket session")
        val userId = decode.id!!.toLong()
        val sender = getSenderStream(session, userId).subscribe()
        val receiver = getReceiverStream(session, userId)
    }.then()

    /**
     * receiver fun
     */
    private suspend fun getReceiverStream(session: WebSocketSession, userId: Long): Mono<Void> {
        return session.receive()
            .filter { it.type == WebSocketMessage.Type.TEXT }
            .map(WebSocketMessage::getPayloadAsText)
            .flatMap {
                objectStringConverter.stringToObject(it, WebSocketEvent::class.java)
            }
            .flatMap { convertedEvent ->
                when (convertedEvent) {
                    is NewChangeEvent -> redisService.publish(
                        "/topic/document",
                        objectMapper.writeValueAsString(convertedEvent)
                    )
                    is CreateFileEvent -> redisService.publish(
                        "/topic/room",
                        objectMapper.writeValueAsString(convertedEvent)
                    )
                    else -> Mono.error(RuntimeException())
                }
            }
            .doOnSubscribe {
                val userSession = userIdToSession[userId]
                if (userSession == null) {
                    val newUserSessions = LinkedList<WebSocketSession>()
                    userIdToSession[userId] = newUserSessions
                }
                userIdToSession[userId]?.add(session)
            }
            .doFinally {
                val userSessions = userIdToSession[userId]
                userSessions?.remove(session)
            }
            .then()
    }

    /**
     * sender fun
     */
    private suspend fun getSenderStream(session: WebSocketSession, userId: Long): Mono<Void> {
        val data = SinkWrapper.sinks.asFlux()
            .filter { sendTo -> sendTo.userId == userId }
            .map { sendTo -> objectMapper.writeValueAsString(sendTo.event) }
            .map { stringObject -> session.textMessage(stringObject) }
            .doOnError { logger.error("$it") }
        return session.send(data)
    }
}