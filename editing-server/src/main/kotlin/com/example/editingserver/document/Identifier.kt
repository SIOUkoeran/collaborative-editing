package com.example.editingserver.document

data class Identifier(
    val digit : Int,
    val client : Int
) {
    companion object{
        fun compare(o1 : Identifier, o2 : Identifier) : Int {
            return if (o1.digit == o2.digit) 0
            else -1
        }
    }

    fun compareIdentifier(identifier1 : Identifier) : Int {
        return if (identifier1.digit < this.digit) {
            -1
        } else if (identifier1.digit > this.digit){
            1
        } else {
            if (identifier1.client < this.client) {
                -1
            }else if (identifier1.client > this.client){
                1
            }else {
                0
            }
        }
    }
}