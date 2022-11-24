package com.example.editingserver.collaborate

import com.fasterxml.jackson.annotation.JsonProperty

data class EditorChange(
    @JsonProperty("type")
    val type : String,
    @JsonProperty("char")
    val char : Char,
    @JsonProperty("line")
    val line : Int
)
