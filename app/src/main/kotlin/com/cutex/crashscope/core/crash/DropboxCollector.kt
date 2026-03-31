package com.cutex.crashscope.core.crash

import android.content.Context
import android.os.DropBoxManager
import com.cutex.crashscope.data.model.CrashEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DropboxCollector {

    suspend fun collect(context: Context): List<CrashEvent> = withContext(Dispatchers.IO) {
        val dropBoxManager = context.getSystemService(Context.DROPBOX_SERVICE) as? DropBoxManager
            ?: return@withContext emptyList()

        val crashes = mutableListOf<CrashEvent>()
        
        val tags = arrayOf("data_app_crash", "system_app_crash", "data_app_anr", "system_app_anr")


        val time = 0L

        for (tag in tags) {
            var entry = dropBoxManager.getNextEntry(tag, time)
            
            while (entry != null) {
                try {
                   
                    val text = entry.getText(8192) // 8Kb
                    
                    if (text != null) {
                        val crash = parseEntry(entry.timeMillis, text, tag)
                        if (crash != null) {
                            crashes.add(crash)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    entry.close()
                }
                entry = dropBoxManager.getNextEntry(tag, entry.timeMillis)
            }
        }
        
        
        crashes.sortedByDescending { it.timestamp }
    }

    private fun parseEntry(timestamp: Long, text: String, tag: String): CrashEvent? {
        val lines = text.lines()
        
        
        val processLine = lines.firstOrNull { it.contains("Process:") } ?: return null
        val packageName = processLine.substringAfter("Process:").trim().substringBefore(" ")


        val exceptionLine = lines.firstOrNull { it.contains("Exception:") || it.contains("Error:") } 
            ?: lines.firstOrNull { it.trim().startsWith("at ") }
            ?: "Unknown Error"

        val exceptionType = exceptionLine.substringBefore(":").trim()

        
        val type = if (tag.contains("anr")) "ANR" else "CRASH"

        return CrashEvent(
            packageName = packageName,
            type = type,
            exceptionType = exceptionType,
            thread = "main", 
            timestamp = timestamp,
            stackTrace = text 
        )
    }
}
