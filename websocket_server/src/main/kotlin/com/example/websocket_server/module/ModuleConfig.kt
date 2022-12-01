package com.example.websocket_server.module

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer
import com.fasterxml.jackson.databind.type.ClassKey
import com.fasterxml.jackson.databind.type.MapType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ConcurrentHashMap

@Configuration
class ModuleConfig {

    @Bean
    fun codeMapModule(): Module {
        return object : Module() {
            override fun version(): Version {
                return Version.unknownVersion()
            }

            override fun getModuleName(): String {
                return "crdt"
            }

            override fun setupModule(context: SetupContext) {
                val cache =
                    ConcurrentHashMap<ClassKey, JsonDeserializer<*>>()
                context.addDeserializers(object : Deserializers.Base() {
                    override fun findMapDeserializer(
                        type: MapType?,
                        config: DeserializationConfig?,
                        beanDesc: BeanDescription?,
                        keyDeserializer: KeyDeserializer?,
                        elementTypeDeserializer: TypeDeserializer?,
                        elementDeserializer: JsonDeserializer<*>?
                    ): JsonDeserializer<*> {
                        return super.findMapDeserializer(
                            type,
                            config,
                            beanDesc,
                            keyDeserializer,
                            elementTypeDeserializer,
                            elementDeserializer
                        )
                    }

                    override fun hasDeserializerFor(
                        config: DeserializationConfig?,
                        valueType: Class<*>?
                    ): Boolean {
                        return cache.containsKey(ClassKey(valueType))
                    }

                    fun addDeserializer(clazz: Class<*>, deserializers: JsonDeserializer<*>) {
                        val key = ClassKey(clazz)
                        cache[key] = deserializers
                    }
                })
            }
        }
    }
}