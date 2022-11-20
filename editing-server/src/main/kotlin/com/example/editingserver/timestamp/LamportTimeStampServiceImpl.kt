package com.example.editingserver.timestamp

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.math.max

@Component
class LamportTimeStampServiceImpl : TimeStampService{
    companion object{
        private val log : Logger = LoggerFactory.getLogger(LamportTimeStampServiceImpl::class.java)
    }

    override fun generateTimestamp(time: Int) : Int{
        return time + 1
    }

    override fun receiveTimeStamp(timestamp: Int, time: Int) : Int{
        return max(timestamp, time) + 1
    }


}