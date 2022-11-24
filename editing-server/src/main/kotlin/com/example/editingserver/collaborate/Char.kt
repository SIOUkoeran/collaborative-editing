package com.example.editingserver.collaborate

import com.fasterxml.jackson.annotation.JsonProperty
import kotlin.math.min

data class Char(
    @JsonProperty("position")
    var position : MutableList<Identifier> = mutableListOf(),
    @JsonProperty("timestamp")
    val timestamp : Int,
    @JsonProperty("value")
    var value : String
) : Comparator<Char> {
    override fun compare(o1: Char, o2: Char): Int {
        for (i : Int in 0 until min(o2.position.size, o1.position.size)){
            val comp = Identifier.compare(o2.position[i], o1.position[i])
            if (comp != 0) {
                return comp
            }
        }
        return if (o2.position.size < o1.position.size) -1
        else if (o2.position.size > o1.position.size) 1
        else 0
    }
}