package com.example.editingserver.redis.service

import com.example.editingserver.service.EditingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.ReactiveSubscription
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Service
import java.nio.channels.Channel
import javax.annotation.PostConstruct

@Service
class RedisService(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>,
    private val editingService : EditingService
) {
    companion object{
        private val log : Logger = LoggerFactory.getLogger(RedisService::class.java)
    }

    /**
     * 공통 pub (String)
     */
    fun publish(topic : String, message: String) {
        log.info("publish message : $message to $topic")
        reactiveRedisTemplate.convertAndSend(topic, message)
    }

    /**
     * 공통 sub (String)
     */
    fun subscribe (channelTopic: String, destination : String) {
        reactiveRedisTemplate.listenTo(ChannelTopic.of(channelTopic))
            .map(ReactiveSubscription.Message<String, String>::getMessage)
            .subscribe { string -> editingService.getMessage(string) }
    }

    /**
     * 문서 작업 방 sub
     */
    fun subscribeCreateDoc (channelTopic: String, destination: String) {
        reactiveRedisTemplate.listenTo(ChannelTopic.of(channelTopic))
            .map(ReactiveSubscription.Message<String, String>::getMessage)
            .subscribe{ message -> editingService.createDoc(message)}
    }

    /**
     * 문서 공동 수정 sub
     */
    fun subscribeUpdateDoc (channelTopic: String){
        reactiveRedisTemplate.listenTo(ChannelTopic.of(channelTopic))
            .map(ReactiveSubscription.Message<String, String>::getMessage)
            .subscribe { message -> editingService.updateDoc(message) }
    }

    @PostConstruct
    fun subscribeWebsocket() {
        subscribe("EDITING_CHANNEL", "/topic/websocket")
        subscribe("DOCUMENT_ROOM_CHANNEL", "/topic/document")
        subscribeCreateDoc("/topic/room", "/topic/document/room")
        subscribeUpdateDoc("topic/document/*")
    }
}