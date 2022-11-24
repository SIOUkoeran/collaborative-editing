package com.example.editingserver.model

import com.example.editingserver.collaborate.Char
import com.example.editingserver.collaborate.DocumentDto
import com.google.common.collect.*
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.persistence.*


@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
data class Document(
    @Column(columnDefinition = "jsonb")
    @Type(type = "jsonb")
    var content: DocumentDto? = null,

    /**
     * char 정보 저장 synchronizedMap
     */
    @Column(name = "value", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    var value: ConcurrentHashMap<Double, Char> = ConcurrentHashMap<Double, Char>()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long ?= null

    @CreationTimestamp
    var createdAt : LocalDateTime = LocalDateTime.now()

    @UpdateTimestamp
    var updatedAt : LocalDateTime ?= null

//    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
//    var people : MutableList<User> = mutableListOf()
}