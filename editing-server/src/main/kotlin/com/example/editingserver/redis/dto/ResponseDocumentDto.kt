package com.example.editingserver.redis.dto

import com.example.editingserver.collaborate.Char
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * server 동기화 후 redis publish 용도 dto
 *
 */
data class ResponseDocumentDto(
    @JsonProperty("crdt")
    val crdt : ArrayList<ArrayList<Char>>,
    @JsonProperty("text")
    val text : String
)
