package com.example.editingserver.collaborate.service

import com.example.editingserver.collaborate.Identifier
import org.springframework.stereotype.Service

@Service
interface CollaborativeService {
    fun generatePositionBetween(position1 : List<Identifier>, position2 : List<Identifier>,
                                site : Int) : List<Identifier>
}