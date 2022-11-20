package com.example.editingserver.timestamp

import org.springframework.stereotype.Service

/**
 * generate timestamp to Char timestamp
 */
@Service
interface TimeStampService {

    fun generateTimestamp(time : Int) : Int
    fun receiveTimeStamp(timestamp : Int, time : Int) : Int
}