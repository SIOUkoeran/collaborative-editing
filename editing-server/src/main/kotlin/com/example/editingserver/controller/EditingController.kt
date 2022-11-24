package com.example.editingserver.controller

import com.example.editingserver.collaborate.service.CollaborativeService
import org.springframework.web.bind.annotation.RestController

@RestController
class EditingController(
    private val collaborativeService: CollaborativeService
) {

}