package com.example.editingserver.document

import kotlin.math.min

data class Char(
    var position : MutableList<Identifier> = mutableListOf(),
    val timestamp : Int,
    val value : String
) {
    companion object {
        fun addIdentifier(client : Int, digit : Int, cur : Char, prev : Char?) : Int{
            return if (prev != null) {
                cur.position.addAll(prev.position)
                cur.position.add(Identifier(digit = digit, client))
                digit + 1
            } else {
                cur.position.add(Identifier(digit = digit, client = client))
                digit + 1
            }
        }
    }

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