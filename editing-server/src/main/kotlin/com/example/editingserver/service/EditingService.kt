package com.example.editingserver.service

import org.springframework.stereotype.Service

@Service
interface EditingService {

    fun getMessage(message : String)
}