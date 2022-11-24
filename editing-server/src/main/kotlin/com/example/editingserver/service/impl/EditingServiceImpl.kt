package com.example.editingserver.service.impl

import com.example.editingserver.collaborate.Char
import com.example.editingserver.collaborate.DocumentDto
import com.example.editingserver.collaborate.Identifier
import com.example.editingserver.collaborate.service.CollaborativeService
import com.example.editingserver.exception.NotFoundDocumentException
import com.example.editingserver.model.Document
import com.example.editingserver.redis.dto.RequestDocumentRoom
import com.example.editingserver.redis.dto.ResponseDocumentDto
import com.example.editingserver.redis.service.RedisService
import com.example.editingserver.service.DocumentService
import com.example.editingserver.service.EditingService
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList
import kotlin.math.floor

@Component
@Transactional(readOnly = true)
class EditingServiceImpl(
    private val collaborativeService: CollaborativeService,
    private val redisService: RedisService,
    private val documentService: DocumentService
) : EditingService {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(EditingServiceImpl::class.java)
        private val objectMapper: ObjectMapper = ObjectMapper()
    }

    override fun getMessage(message: String) {
        log.info("get Message : $message")

        TODO("Not yet implemented")
    }

    override fun createDoc(message: String) {

        log.info("request create room")
        val document: RequestDocumentRoom =
            objectMapper.readValue(message, RequestDocumentRoom::class.java)
        log.info("publish created document room ${document.roomId}")
        redisService.publish("/topic/room", objectMapper.writeValueAsString(document))
    }

    override fun updateDoc(message: String): Document {
        val document =
            objectMapper.readValue(message, DocumentDto::class.java) ?: throw RuntimeException()
        val findDocument = this.documentService.getDocument(document.id)
        val charMap = findDocument.value
        for (change in document.editorChange!!){
            when(change.type) {
                "add" -> {
                    val key = convertIdentifierToKey(change.char.position, change.line)
                    charMap[key] = change.char
                }"remove" -> {
                    val key = convertIdentifierToKey(change.char.position, change.line)
                    charMap.remove(key)
                }
            }
        }

        findDocument.value = charMap
        val crdt : ArrayList<ArrayList<Char>> = toArrayList(charMap)
        val stringBuilder = StringBuilder()
        for (line in crdt) {
            val lineText = line.toString()
            stringBuilder.append(lineText).append("\n")
        }
        val response = ResponseDocumentDto(crdt, stringBuilder.toString())
        log.debug("sync document")
        this.redisService.publish(
            "/topic/document/" + document.id,
            objectMapper.writeValueAsString(response)
        )
        return findDocument
    }

    /**
     * hashtable to arrayList
     */
    private fun toArrayList(charHashtable: ConcurrentHashMap<Double, Char>): ArrayList<ArrayList<Char>> {
        val keys = charHashtable.keys.sorted()
        val returnList = java.util.ArrayList<java.util.ArrayList<Char>>()
        keys.stream()
            .forEach { key ->
                val char = charHashtable[key] ?: throw NotFoundDocumentException()
                val floorKey = floor(key).toInt()
                if (returnList.size > floorKey) {
                    returnList[floorKey].add(char)
                }else{
                    returnList.add(floorKey, arrayListOf(char))
                }
            }
        return returnList
    }

    private fun convertIdentifierToKey(identifier : List<Identifier>, line : Int) : Double{
        val stringBuilder = StringBuilder()
        stringBuilder.append(line).append(".")
        identifier.stream()
            .forEach { ident ->
                stringBuilder.append(ident)
            }
        return stringBuilder.toString().toDouble()
    }


    override fun saveDoc(message: String) {
        val requestDocument = objectMapper.readValue(message, DocumentDto::class.java)
        this.documentService.saveDocument(requestDocument)
        TODO("Not yet implemented")
    }
}