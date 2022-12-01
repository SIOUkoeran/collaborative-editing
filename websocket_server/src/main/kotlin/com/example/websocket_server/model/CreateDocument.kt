package com.example.websocket_server.model

import java.time.LocalDateTime

class CreateDocument(
    userId: Long,
    documentId: Long,
    date: LocalDateTime,
) : DocumentDto(
    userId = listOf(userId),
    documentId = documentId,
    date = date
)