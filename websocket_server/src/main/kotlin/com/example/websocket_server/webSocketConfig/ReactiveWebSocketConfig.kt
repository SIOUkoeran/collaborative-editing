package com.example.websocket_server.webSocketConfig

import com.example.websocket_server.exception.InvalidJwtTokenException
import com.example.websocket_server.login.jwtutils.JWTProperties
import com.example.websocket_server.login.jwtutils.JWTUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.HandlerResult
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import java.util.*
import java.util.function.Predicate

@Configuration
class ReactiveWebSocketConfig(
    private val jwtProperties: JWTProperties
) {

    @Bean
    fun webSocketHandlerMapping(webSocketHandler: DefaultWebSocketHandler)
            : HandlerMapping {
        val map: MutableMap<String, WebSocketHandler> = hashMapOf()
        map["/ws/**"] = webSocketHandler

        val handlerMapping = SimpleUrlHandlerMapping()
        handlerMapping.setCorsConfigurations(
            Collections.singletonMap(
                "*",
                CorsConfiguration().applyPermitDefaultValues()
            )
        )
        handlerMapping.order = 1
        handlerMapping.urlMap = map
        return handlerMapping
    }

    @Bean
    fun getWebsocketHandlerAdapter(): WebSocketHandlerAdapter {
        val handshakeWebSocketService = HandshakeWebSocketService()
        handshakeWebSocketService.sessionAttributePredicate =
            Predicate { true }
        val wsha: WebSocketHandlerAdapter =
            object : WebSocketHandlerAdapter(handshakeWebSocketService) {
                override fun handle(
                    exchange: ServerWebExchange,
                    handler: Any
                ): Mono<HandlerResult> {
                    val attributes = exchange.attributes
                    val token = attributes["Authorization"].toString()
                    val decode = JWTUtils.decode(
                        token = token,
                        issuer = jwtProperties.issuer,
                        secret = jwtProperties.secret
                    )
                    if (decode.claims["userId"]?.asLong() == null) {
                        throw InvalidJwtTokenException()
                    }
                    exchange.session.subscribe { session: WebSession ->
                        session.attributes.putAll(attributes)
                    }
                    return super.handle(exchange, handler)
                }
            }
        return wsha
    }

}