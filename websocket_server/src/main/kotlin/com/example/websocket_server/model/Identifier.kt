package com.example.websocket_server.model

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
data class Identifier(
    val digit: Int,
    val site: Int
)