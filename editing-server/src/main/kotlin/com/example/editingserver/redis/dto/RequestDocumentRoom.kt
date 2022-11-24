package com.example.editingserver.redis.dto

data class RequestDocumentRoom(
    val userId : Long,
    val roomId : Long,
    val documentsId : List<Long> ?= null
)
