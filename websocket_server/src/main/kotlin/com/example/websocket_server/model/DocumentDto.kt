package com.example.websocket_server.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDateTime

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
open class DocumentDto(
    val userId: List<Long>,
    val documentId: Long,
    @field:JsonSerialize(using = LocalDateTimeSerializer::class)
    @field:JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val date: LocalDateTime
)