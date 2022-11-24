package com.example.editingserver.service

import com.example.editingserver.collaborate.DocumentDto
import com.example.editingserver.model.Document
import org.springframework.stereotype.Service

@Service
interface DocumentService {
    fun createDocumentRoom(userId : Long) : Document
    fun saveDocument(requestDocument : DocumentDto) : Document
    fun getDocument(documentId : Long) : Document
}