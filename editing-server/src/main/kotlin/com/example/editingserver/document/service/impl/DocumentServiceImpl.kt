package com.example.editingserver.document.service.impl

import com.example.editingserver.document.Identifier
import com.example.editingserver.document.Identifier.Companion.cons
import com.example.editingserver.document.Identifier.Companion.fromIdentifierList
import com.example.editingserver.document.Identifier.Companion.getDigitList
import com.example.editingserver.document.Identifier.Companion.head
import com.example.editingserver.document.Identifier.Companion.increment
import com.example.editingserver.document.Identifier.Companion.rest
import com.example.editingserver.document.Identifier.Companion.subtractGreaterThan
import com.example.editingserver.document.Identifier.Companion.toIdentifierList
import com.example.editingserver.document.service.DocumentService
import com.example.editingserver.document.crdt.dto.CRDTDocument
import com.example.editingserver.document.crdt.dto.DocElements
import com.example.editingserver.exception.NotFoundDocumentException
import com.example.editingserver.exception.NotFoundElementsException
import com.example.editingserver.model.Document
import com.example.editingserver.redis.service.RedisService
import com.example.editingserver.repository.DocumentRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import kotlin.math.min
import kotlin.math.pow
import kotlin.streams.toList


@Component
@Transactional
class DocumentServiceImpl(
    private val documentRepository: DocumentRepository,
    private val redisService: RedisService
) : DocumentService {
    companion object{
        private val log : Logger = LoggerFactory.getLogger(DocumentServiceImpl::class.java)
    }

    @Transactional(readOnly = true)
    override fun findDocElements(doc : CRDTDocument, seq : Int) : DocElements {
        if (seq >= doc.contents.size)
            throw NotFoundElementsException()
        return doc.contents[seq]
    }

    @Transactional(readOnly = true)
    fun readDoc(id : Long) : Document{
        return this.documentRepository.findByIdOrNull(id)
            ?: throw NotFoundDocumentException()
    }

    override fun updateDoc(id : Long,  modifiedDoc : CRDTDocument): CRDTDocument? {
        val originDoc = readDoc(id)
        val flag : Boolean = modifiedDoc.update(originDoc.content)
        if (flag)
            return modifiedDoc
        else
            return null
        TODO("Not yet implemented")
    }

    override fun createDoc(roomId: String) : Document{
        val savedDocument : Document = this.documentRepository.save(Document(CRDTDocument(length = 0)))
        log.info("saved success id : $savedDocument.id")
        return savedDocument
    }

    override fun generatePositionBetween(position1: List<Identifier>,
                                         position2: List<Identifier>,
                                         site : Int) : List<Identifier> {

        val p1Digits = getDigitList(position1)
        val p2Digits = getDigitList(position2)
        val head1 = head(position1) ?: Identifier(0, site)
        val head2 = head(position2) ?: Identifier(256, site)

        if (head1 != head2) {
            val n1 = fromIdentifierList(position1)
            val n2 = fromIdentifierList(position2)
            val delta = subtractGreaterThan(n2, n1)

            val next : List<Int> = increment(n1, delta)
            return toIdentifierList(next, position1, position2, site)
        }else {
            if (head1.client < head2.client) {
                return cons(head1, generatePositionBetween(rest(position1), mutableListOf(), site), site)
            }else if (head1.client == head2.client) {
                return cons(head1, generatePositionBetween(rest(position1), rest(position2), site), site)
            } else {
                throw RuntimeException("invalid site ordering")
            }
        }
    }



}