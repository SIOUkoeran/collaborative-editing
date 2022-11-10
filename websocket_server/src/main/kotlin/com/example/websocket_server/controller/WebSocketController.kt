package com.example.websocket_server.controller

import com.example.websocket_server.redis.service.RedisService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller


@Controller
class WebSocketController(private val redisService : RedisService) {
    @MessageMapping("/greet")
    fun greetMessage(@Payload message : String) {
        redisService.publish("GREETING_CHANNEL_OUTBOUND", message)
    }
}