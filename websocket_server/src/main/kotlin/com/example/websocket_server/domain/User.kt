package com.example.websocket_server.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("users")
data class User(
    @Id
    val id: Long? = null,

    @Column
    val email: String,

    @Column
    val password: String,

    @Column
    val username: String,

    @CreatedDate
    @Column("created_at")
    val createdAt: LocalDateTime? = null,

    @LastModifiedBy
    @Column("updated_at")
    val updatedAt: LocalDateTime? = null
)
