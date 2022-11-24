package com.example.editingserver.service.impl

import com.example.editingserver.collaborate.Char
import com.example.editingserver.collaborate.DocumentDto
import com.example.editingserver.collaborate.EditorChange
import com.example.editingserver.collaborate.Identifier
import com.example.editingserver.collaborate.service.CollaborativeService
import com.example.editingserver.collaborate.service.impl.CollaborativeServiceImpl
import com.example.editingserver.exception.NotFoundDocumentException
import com.example.editingserver.exception.NotFoundElementsException
import com.example.editingserver.model.Document
import com.example.editingserver.redis.service.RedisService
import com.example.editingserver.service.DocumentService
import com.example.editingserver.service.EditingService
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.LinkedListMultimap
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashMap
import kotlin.math.floor

@ExtendWith(MockitoExtension::class)
internal class EditingServiceTest{

    private val redisService: RedisService = Mockito.mock(RedisService::class.java)
    private val documentService: DocumentService = Mockito.mock(DocumentService::class.java)

    private val collaborativeService : CollaborativeService = CollaborativeServiceImpl()
    private val editingService : EditingService = EditingServiceImpl(collaborativeService, redisService, documentService)

    private val log : Logger = LoggerFactory.getLogger(EditingServiceTest::class.java)

    val objectMapper: ObjectMapper = ObjectMapper()

    private fun makeSuperDocumentId1() : Document{
        val documentId = 1L
        val charList : MutableList<Char> = mutableListOf()
        val char1 : Char = Char(timestamp = 1, value = "s")
        val char2 : Char = Char(timestamp = 1, value = "u")
        val char3 : Char = Char(timestamp = 1, value = "p")
        val char4 : Char = Char(timestamp = 1, value = "e")
        val char5 : Char = Char(timestamp = 1, value = "r")
        charList.add(char1)
        charList.add(char2)
        charList.add(char3)
        charList.add(char4)
        charList.add(char5)
        val document : DocumentDto = DocumentDto(documentId, charList, 1)
        val documentString = objectMapper.writeValueAsString(document)
        val updateDoc = editingService.updateDoc(documentString)
        return updateDoc!!
    }


    @Test
    fun tempTest() {
        val hashtable = Hashtable<Double, Char>()
        hashtable.put(
            0.1,
            Char(position = mutableListOf(Identifier(digit = 1, client = 1)),
                timestamp = 1,
                value = "s"
            )
        )
        hashtable.put(
            0.2,
            Char(position = mutableListOf(Identifier(digit = 2, client = 1)),
                timestamp = 1,
                value = "u"
            )
        )
        hashtable.put(
            0.3,
            Char(position = mutableListOf(Identifier(digit = 3, client = 1)),
                timestamp = 1,
                value = "p"
            )
        )
        hashtable.put(
            1.1,
            Char(position = mutableListOf(Identifier(digit = 1, client = 1)),
                timestamp = 1,
                value = "d"
            )
        )
        hashtable.put(
            1.2,
            Char(position = mutableListOf(Identifier(digit = 1, client = 1)),
                timestamp = 1,
                value = "f"
            )
        )
        val keys = hashtable.keys.sorted()
        log.info("$keys")
        val returnList = ArrayList<ArrayList<Char>>()
        val atomicInteger = AtomicInteger(1)
        keys.stream()
            .forEach { key ->
                val char = hashtable[key] ?: throw NotFoundDocumentException()
                val floorKey = floor(key).toInt()
                if (returnList.size > floorKey) {
                    returnList[floorKey].add(char)
                }else{
                    returnList.add(floorKey, arrayListOf(char))
                }
            }
        returnList.get(0).stream()
            .forEach {s -> log.info(s.value) }
        returnList.get(1).stream()
            .forEach { s -> log.info(s.value) }

    }
}