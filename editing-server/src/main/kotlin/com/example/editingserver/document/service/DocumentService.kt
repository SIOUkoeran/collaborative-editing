package com.example.editingserver.document.service

import com.example.editingserver.document.Identifier
import com.example.editingserver.document.crdt.dto.CRDTDocument
import com.example.editingserver.document.crdt.dto.DocElements
import com.example.editingserver.model.Document
import org.springframework.stereotype.Service

@Service
interface DocumentService {

    fun findDocElements(doc : CRDTDocument, seq : Int) : DocElements
    fun updateDoc(id : Long, modifiedDoc : CRDTDocument) : CRDTDocument?
    fun createDoc(roomId: String) : Document
    fun generatePositionBetween(position1 : List<Identifier>, position2 : List<Identifier>,
                                site : Int) : List<Identifier>
}