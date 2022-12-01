package com.example.websocket_server

import org.springframework.stereotype.Component
import reactor.core.publisher.Sinks

@Component
object SinkWrapper {
    val sinks: Sinks.Many<SendTo> =
        Sinks.many().multicast().onBackpressureBuffer()
}

data class SendTo(
    val userId: Long,
    val event: WebSocketEvent
)