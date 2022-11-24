package com.example.editingserver.service.impl

import com.example.editingserver.collaborate.DocumentDto
import com.example.editingserver.exception.NotFoundDocumentException
import com.example.editingserver.model.Document
import com.example.editingserver.repository.DocumentRepository
import com.example.editingserver.service.DocumentService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class DocumentServiceImpl(
    private val documentRepository: DocumentRepository
) : DocumentService{
    companion object{
        private val log : Logger = LoggerFactory.getLogger(DocumentServiceImpl::class.java)
    }
    override fun createDocumentRoom(userId: Long) : Document{
        log.info("request create document room $userId")
        val savedDocument = documentRepository.save(Document())
        log.info("success save document $savedDocument.id")
        return savedDocument
    }

    override fun saveDocument(requestDocument: DocumentDto): Document {
        log.info("request save document in db ${requestDocument.id}")
        val save = this.documentRepository.save(Document(requestDocument))
        log.info("success save document in db ${requestDocument.id}")
        return save
    }

    @Transactional(readOnly = false)
    override fun getDocument(documentId: Long): Document {
        val document = this.documentRepository.findByIdOrNull(documentId)
                ?: throw NotFoundDocumentException()
        log.info("success request : $document")
        return document
    }
}