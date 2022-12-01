package com.example.websocket_server.exception

data class ErrorResponse(
    val code: Int,
    val message: String,
)