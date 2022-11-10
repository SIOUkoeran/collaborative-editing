package com.example.websocket_server.controller

import com.example.websocket_server.notification.dto.NewMessageRequest
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/notification")
class NotificationController(private val template : SimpMessagingTemplate) {

    @PostMapping
    fun newMessage(@RequestBody request : NewMessageRequest) {
        template.convertAndSend(request.topic, request.message)
    }
}