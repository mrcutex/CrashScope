package com.cutex.crashscope.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class CrashUiModel(
    val id: Long,
    val packageName: String,
    val appName: String,
    val exceptionType: String,
    val formattedTime: String,
    val isAnr: Boolean,
    val originalEvent: CrashEvent
)
