package com.example.editingserver.redis.dto

import com.example.editingserver.collaborate.DocumentDto

data class DocumentRedisDto(
    val content : DocumentDto ?= null,
    val documentId : Long ?= null,
)
