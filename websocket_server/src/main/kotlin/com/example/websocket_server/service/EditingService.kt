package com.example.websocket_server.service

import com.example.websocket_server.model.CreateDocument
import com.example.websocket_server.model.DocumentDto
import com.example.websocket_server.model.UpdateDocument
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
interface EditingService {
    /**
     * send document message
     */
    suspend fun sendDocumentMessage(documentDto: DocumentDto): Mono<Void>

    /**
     * send request to create a document
     */
    suspend fun sendCreateDocumentMessage(documentDto: CreateDocument): Mono<Void>

    /**
     * send request for document changes to client
     */
    suspend fun sendUpdateDocumentMessage(documentDto: UpdateDocument): Mono<Void>
}