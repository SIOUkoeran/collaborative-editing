package com.example.editingserver.document

import kotlin.math.min

data class Char(
    val position : MutableList<Identifier> = mutableListOf(),
    val port : Int,
    val value : String
) {

    fun comparePosition(p1 : MutableList<Identifier>) : Int{
        for (i : Int in 0 until min(p1.size, this.position.size)){
            val comp = Identifier.compare(p1[i], this.position[i])
            if (comp != 0) {
                return comp
            }
        }
        return if (p1.size < this.position.size) -1
            else if (p1.size > this.position.size) 1
            else 0
    }
}