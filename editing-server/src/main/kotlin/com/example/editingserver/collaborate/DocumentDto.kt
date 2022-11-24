package com.example.editingserver.collaborate

import com.fasterxml.jackson.annotation.JsonProperty

data class DocumentDto(
    @JsonProperty("id")
    val id : Long,
    @JsonProperty("value")
    var value : MutableList<Char>? = null,
    @JsonProperty("site")
    val site : Int,
    @JsonProperty("editorChange")
    val editorChange: List<EditorChange> ?= null,
    @JsonProperty("text")
    val text : List<String> ?= null
)
