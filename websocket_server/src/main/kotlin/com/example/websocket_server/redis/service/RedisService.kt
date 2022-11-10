package com.example.websocket_server.redis.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.ReactiveSubscription
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class RedisService(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>,
    private val webSocketTemplate: SimpMessagingTemplate
) {

    private val log : Logger = LoggerFactory.getLogger(RedisService::class.java)

    fun publish (topic : String, message : String) {
        log.info("publish message : $message to $topic")
        reactiveRedisTemplate.convertAndSend(topic, message).subscribe()
    }

    fun subscribe (channelTopic: String, destination : String) {
        reactiveRedisTemplate.listenTo(ChannelTopic.of(channelTopic))
            .map ( ReactiveSubscription.Message<String, String>::getMessage )
            .subscribe{ message -> webSocketTemplate.convertAndSend(destination, message)}
    }

    @PostConstruct
    fun subscribe() {
        subscribe("GREEN_CHANNEL_INBOUND", "/topic/greetings")
    }
}