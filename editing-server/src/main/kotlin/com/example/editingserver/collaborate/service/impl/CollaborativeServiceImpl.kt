package com.example.editingserver.collaborate.service.impl

import com.example.editingserver.collaborate.Identifier
import com.example.editingserver.collaborate.Identifier.Companion.cons
import com.example.editingserver.collaborate.Identifier.Companion.fromIdentifierList
import com.example.editingserver.collaborate.Identifier.Companion.getDigitList
import com.example.editingserver.collaborate.Identifier.Companion.head
import com.example.editingserver.collaborate.Identifier.Companion.increment
import com.example.editingserver.collaborate.Identifier.Companion.rest
import com.example.editingserver.collaborate.Identifier.Companion.subtractGreaterThan
import com.example.editingserver.collaborate.Identifier.Companion.toIdentifierList
import com.example.editingserver.collaborate.service.CollaborativeService
import com.example.editingserver.redis.service.RedisService
import com.example.editingserver.repository.DocumentRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
@Transactional
class CollaborativeServiceImpl() : CollaborativeService {
    companion object{
        private val log : Logger = LoggerFactory.getLogger(CollaborativeServiceImpl::class.java)
    }

    override fun generatePositionBetween(position1: List<Identifier>,
                                         position2: List<Identifier>,
                                         site : Int) : List<Identifier> {

        val head1 = head(position1) ?: Identifier(0, site)
        val head2 = head(position2) ?: Identifier(Int.MAX_VALUE, site)
        if (head1 != head2) {
            val n1 = fromIdentifierList(position1)
            val n2 = fromIdentifierList(position2)
            val delta = subtractGreaterThan(n2, n1)
            log.info("delta : $delta")
            val next : List<Int> = increment(n1, delta)
            return toIdentifierList(next, position1, position2, site)
        }else {
            return if (head1.client < head2.client) {
                cons(head1, generatePositionBetween(rest(position1), mutableListOf(), site), site)
            }else if (head1.client == head2.client) {
                cons(head1, generatePositionBetween(rest(position1), rest(position2), site), site)
            } else {
                throw RuntimeException("invalid site ordering")
            }
        }
    }
}