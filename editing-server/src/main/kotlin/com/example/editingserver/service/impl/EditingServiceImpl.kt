package com.example.editingserver.service.impl

import com.example.editingserver.service.EditingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class EditingServiceImpl : EditingService {
    companion object {
        private val log : Logger = LoggerFactory.getLogger(EditingServiceImpl::class.java)
    }
    override fun getMessage(message: String) {
        log.info("get Message : $message")

        TODO("Not yet implemented")
    }
}