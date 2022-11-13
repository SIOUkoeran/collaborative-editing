package com.example.editingserver.redis.service

import com.example.editingserver.service.EditingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.ReactiveSubscription
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class RedisService(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>,
    private val editingService : EditingService
) {
    companion object{
        private val log : Logger = LoggerFactory.getLogger(RedisService::class.java)
    }

    fun publish(topic : String, message: String) {
        log.info("publish message : $message to $topic")
        reactiveRedisTemplate.convertAndSend(topic, message)
    }

    fun subscribe (channelTopic: String, destination : String) {
        reactiveRedisTemplate.listenTo(ChannelTopic.of(channelTopic))
            .map(ReactiveSubscription.Message<String, String>::getMessage)
            .subscribe { string -> editingService.getMessage(string) }
    }

    @PostConstruct
    fun subscribeWebsocket() {
        subscribe("EDITING_CHANNEL", "/topic/websocket")
    }
}