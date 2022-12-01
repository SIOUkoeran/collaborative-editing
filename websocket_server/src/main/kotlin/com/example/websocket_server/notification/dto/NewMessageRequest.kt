package com.example.websocket_server.notification.dto

data class NewMessageRequest(
    val topic: String,
    val message: String
) {
}