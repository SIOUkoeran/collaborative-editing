package com.example.websocket_server

import com.example.websocket_server.model.Char
import com.example.websocket_server.model.DocumentDto
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.concurrent.ConcurrentHashMap

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
sealed class WebSocketEvent

data class NewChangeEvent(
    val docId: Long,
    val payload: DocumentDto,
) : WebSocketEvent()

data class CreateFileEvent(
    val docId: Long,
    val userId: Long,
) : WebSocketEvent()