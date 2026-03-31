package com.cutex.crashscope.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cutex.crashscope.core.crash.DropboxCollector
import com.cutex.crashscope.data.SettingsManager 
import com.cutex.crashscope.data.model.CrashUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<List<CrashUiModel>>(emptyList())
    val uiState = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    
    private val settings = SettingsManager(application)
    private val pm = application.packageManager
    private val appNameCache = mutableMapOf<String, String>()

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val rawEvents = DropboxCollector.collect(getApplication())
                
                
                val currentTime = System.currentTimeMillis()
                val oneDayMillis = 24 * 60 * 60 * 1000L
                
                val filteredEvents = rawEvents.filter { event ->
                    
                    val isOld = (currentTime - event.timestamp) > oneDayMillis
                    if (settings.autoDeleteOldLogs && isOld) return@filter false
                    
                    
                    if (!settings.showSystemApps && event.packageName.startsWith("android")) return@filter false
                    
                    true
                }

                val uiModels = filteredEvents.map { event ->
                    val uniqueId = event.timestamp + event.packageName.hashCode()
                    CrashUiModel(
                        id = uniqueId,
                        packageName = event.packageName,
                        appName = getAppName(event.packageName),
                        exceptionType = event.exceptionType,
                        formattedTime = formatDate(event.timestamp),
                        isAnr = event.type == "ANR",
                        originalEvent = event.copy(id = uniqueId)
                    )
                }
                _uiState.value = uiModels
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    
    
    fun getCrashById(id: Long): CrashUiModel? {
        return _uiState.value.find { it.id == id }
    }

    private fun getAppName(packageName: String): String {
        return appNameCache.getOrPut(packageName) {
            try {
                val info = pm.getApplicationInfo(packageName, 0)
                pm.getApplicationLabel(info).toString()
            } catch (e: Exception) {
                packageName
            }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        return when {
            diff < 60000 -> "Just now"
            diff < 3600000 -> "${diff / 60000} min ago"
            diff < 86400000 -> "${diff / 3600000} hours ago"
            else -> SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(timestamp))
        }
    }
}
