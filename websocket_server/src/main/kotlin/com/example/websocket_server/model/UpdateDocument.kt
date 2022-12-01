package com.example.websocket_server.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.MapDeserializer
import com.fasterxml.jackson.databind.ser.std.MapSerializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

class UpdateDocument(
    userId: List<Long>,
    documentId: Long,
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    date: LocalDateTime,

    @JsonDeserialize(using = MapDeserializer::class)
    @JsonSerialize(using = MapSerializer::class)
    crdt: ConcurrentHashMap<String, Char>,
    content: String,
) : DocumentDto(userId, documentId, date = date)