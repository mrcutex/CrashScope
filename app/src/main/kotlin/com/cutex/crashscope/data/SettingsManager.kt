package com.cutex.crashscope.data

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("crash_settings", Context.MODE_PRIVATE)

    
    var autoDeleteOldLogs: Boolean
        get() = prefs.getBoolean("auto_delete", false)
        set(value) = prefs.edit().putBoolean("auto_delete", value).apply()

  
    var showSystemApps: Boolean
        get() = prefs.getBoolean("show_system", true)
        set(value) = prefs.edit().putBoolean("show_system", value).apply()
        
        
        
var isOnboardingCompleted: Boolean
    get() = prefs.getBoolean("onboarding_complete", false)
    set(value) = prefs.edit().putBoolean("onboarding_complete", value).apply()
    
}
