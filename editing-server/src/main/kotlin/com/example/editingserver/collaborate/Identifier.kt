package com.example.editingserver.collaborate

import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.math.max
import kotlin.math.pow
import kotlin.streams.toList

data class Identifier(
    @JsonProperty("digit")
    val digit : Int,
    @JsonProperty("client")
    val client : Int
) : Comparable<Identifier>{
    companion object{
        private val log : Logger = LoggerFactory.getLogger(Identifier::class.java)
        fun compare(o1 : Identifier, o2 : Identifier) : Int {
            return if (o1.digit == o2.digit) 0
            else -1
        }
        fun rest(position1: List<Identifier>): List<Identifier> {
            val result = mutableListOf<Identifier>()
            result.addAll(position1)
            result.removeFirst()
            return result
        }

        fun subtractGreaterThan(n1: List<Int>, n2: List<Int>): MutableList<Int>{
            val n1ToInt = toInt(n1)
            val n2ToInt = toInt(n2)
            return if (n1ToInt < n2ToInt) {
                (n2ToInt - n1ToInt).toString()
                    .map { c ->
                        Character.getNumericValue(c)
                    }.toMutableList()
            } else {
                (n1ToInt - n2ToInt).toString()
                    .map { c ->
                        Character.getNumericValue(c)
                    }
                    .toMutableList()
            }
        }

        private fun toInt(n1 : List<Int>) : Int {
            var n1ToInt = 0
            for (i : Int in n1.indices) {
                n1ToInt += n1[i] * (10.0).pow(n1.size - i - 1).toInt()
            }
            return n1ToInt
        }

        fun cons(head1: Identifier, generatePositionBetween: List<Identifier>, client : Int) : List<Identifier> {
            val result = mutableListOf<Identifier>()
            result.add(Identifier(head1.digit, client))
            for (ident : Identifier in generatePositionBetween) {
                result.add(Identifier(digit = ident.digit, client))
            }
            return result
        }

        fun toIdentifierList(
            next: List<Int>,
            position1: List<Identifier>,
            position2: List<Identifier>,
            site: Int
        ) : List<Identifier> {
            return next.mapIndexed { index, digit ->
                if (index == next.size - 1) {
                    Identifier(digit, site)
                } else if (index < position1.size && digit == position1[index].digit) {
                    Identifier(digit = digit, client = position1[index].client)
                } else if (index < position2.size && digit == position2[index].digit) {
                    Identifier(digit = digit, client = position2[index].client)
                } else {
                    Identifier(digit = digit, client = site)
                }
            }.toMutableList()
        }


        fun head(p : List<Identifier>) : Identifier? {
            if (p.isEmpty())
                return null
            p.sorted()
            return p.first()
        }

        fun getDigitList(p : List<Identifier>) : List<Int> {
            return p.stream()
                .map { identifier -> identifier.digit }
                .toList()
        }

        fun fromIdentifierList(identifiers : List<Identifier>) : List<Int>{
            return identifiers.stream()
                .map { identifier -> identifier.digit }
                .toList()
        }

        fun increment(n1 : List<Int>, delta : List<Int>) : List<Int> {
            val firstDigit : Int = delta.indexOfFirst { x -> x != 0 }
            val inc : MutableList<Int> = delta.slice(0 until firstDigit).toMutableList()
            inc.add(0)
            inc.add(1)
            log.info("inc : $inc")
            val v1 = add(n1, inc)
            return  if (v1[v1.size - 1] == 0) add(v1, inc)
            else v1
        }

        private fun add(n1 : List<Int>, n2 : List<Int>) : MutableList<Int> {
            val n1toInt = toInt(n1) / 10.0.pow(n1.size)
            val n2toInt = toInt(n2) / 10.0.pow(n2.size)
            val result = ((n1toInt + n2toInt) * 10.0.pow(max(n1.size, n2.size))).toInt()
            log.info("n1toInt $n1toInt n2toInt $n2toInt result : $result")
            return result.toString().map{
                    c -> Character.getNumericValue(c)
            }.toMutableList()
        }
    }

    override fun compareTo(other: Identifier): Int {
        return if (other.digit < this.digit) {
            -1
        } else if (other.digit > this.digit){
            1
        } else {
            if (other.client < this.client) {
                -1
            }else if (other.client > this.client){
                1
            }else {
                0
            }
        }
    }
}