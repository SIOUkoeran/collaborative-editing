package com.example.editingserver.redis.config

import com.example.editingserver.redis.dto.DocumentRedisDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext

@Configuration
class RedisConfig() {

    @Bean
    fun reactiveRedisTemplate(factory: LettuceConnectionFactory): ReactiveRedisTemplate<String, String> {
        val serializer = Jackson2JsonRedisSerializer(String::class.java)
        val builder = RedisSerializationContext.newSerializationContext<String, String>()
        val context = builder.value(serializer).build()
        return ReactiveRedisTemplate(factory, context)
    }

    @Bean
    fun reactiveDocumentTemplate(factory: LettuceConnectionFactory) : ReactiveRedisTemplate<String, DocumentRedisDto> {
        val serializer = Jackson2JsonRedisSerializer(DocumentRedisDto::class.java)
        val builder = RedisSerializationContext.newSerializationContext<String, DocumentRedisDto>()
        val context = builder.value(serializer).build()
        return ReactiveRedisTemplate(factory, context)
    }
}