package com.example.editingserver.repository


import com.example.editingserver.model.Document
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository : JpaRepository<Document , Long>{
}