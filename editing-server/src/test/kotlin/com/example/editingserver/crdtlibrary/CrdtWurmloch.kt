package com.example.editingserver.crdtlibrary

import com.netopyr.wurmloch.crdt.GSet
import com.netopyr.wurmloch.store.CrdtStore
import com.netopyr.wurmloch.store.LocalCrdtStore
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest

class CrdtWurmloch {

    companion object{
        private val log : Logger = LoggerFactory.getLogger(CrdtWurmloch::class.java)
    }
    @Test
    fun `CrdtLasWinTest`() {

        val crdtStore1 : LocalCrdtStore = LocalCrdtStore()
        val crdtStore2 : LocalCrdtStore = LocalCrdtStore()

        crdtStore2.connect(crdtStore1)
        val replicas1 = crdtStore1.createGSet<String>("ID_1")
        val replicas2 = crdtStore2.findGSet<String>("ID_1").get()


        replicas1.add("abc")
        replicas1.add("abc")
        replicas2.add("abcd")

        Assertions.assertThat(replicas1).contains("abcd")
        Assertions.assertThat(replicas2).contains("abc")

        log.info("replicas1 size : ${replicas1.size}")
        System.out.println("replicas 1 size : " + replicas1.size)
    }

    @Test
    fun `LWMTest`() {
        val crdtStore1 : LocalCrdtStore = LocalCrdtStore()
        val crdtStore2 : LocalCrdtStore = LocalCrdtStore()
        crdtStore1.connect(crdtStore1)

        val replicas1 = crdtStore1.createLWWRegister<String>("ID_1")
        val replicas2 = crdtStore2.findLWWRegister<String>("ID_1")

        replicas1.set("abc")
        replicas1.set("abcd")

        log.info("replicas 1 size ${replicas1.get()}")
    }
}