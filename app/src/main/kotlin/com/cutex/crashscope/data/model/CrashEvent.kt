package com.cutex.crashscope.data.model


data class CrashEvent(
    val id: Long = 0, 
    val packageName: String,
    val type: String,
    val exceptionType: String,
    val thread: String,
    val timestamp: Long,
    val stackTrace: String
)
