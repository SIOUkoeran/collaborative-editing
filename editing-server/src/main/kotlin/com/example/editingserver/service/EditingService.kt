package com.example.editingserver.service

import com.example.editingserver.collaborate.DocumentDto
import com.example.editingserver.model.Document
import org.springframework.stereotype.Service

@Service
interface EditingService {

    fun getMessage(message : String)
    fun createDoc(message : String)
    fun updateDoc(message : String) : Document?
    fun saveDoc(message : String)
}