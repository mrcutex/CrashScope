package com.cutex.crashscope.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.SystemSecurityUpdate
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cutex.crashscope.BuildConfig
import com.cutex.crashscope.data.SettingsManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.foundation.clickable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val context = LocalContext.current
    val settings = remember { SettingsManager(context) }

    var autoDelete by remember { mutableStateOf(settings.autoDeleteOldLogs) }
    var showSystem by remember { mutableStateOf(settings.showSystemApps) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(
                    "Preferences",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                ListItem(
                    headlineContent = { Text("Auto-hide old logs") },
                    supportingContent = { Text("Hide crash logs older than 24 hours") },
                    leadingContent = { Icon(Icons.Rounded.DeleteSweep, null) },
                    trailingContent = {
                        Switch(
                            checked = autoDelete,
                            onCheckedChange = {
                                autoDelete = it
                                settings.autoDeleteOldLogs = it
                            }
                        )
                    }
                )
            }

            item {
                HorizontalDivider()
            }

            item {
                ListItem(
                    headlineContent = { Text("Show System Apps") },
                    supportingContent = { Text("Include logs from Android system processes") },
                    leadingContent = { Icon(Icons.Rounded.SystemSecurityUpdate, null) },
                    trailingContent = {
                        Switch(
                            checked = showSystem,
                            onCheckedChange = {
                                showSystem = it
                                settings.showSystemApps = it
                            }
                        )
                    }
                )
            }

            item {
                HorizontalDivider()
            }

           item {
    ListItem(
        headlineContent = { Text("About") },
        supportingContent = { Text("App info, version and links") },
        leadingContent = { Icon(Icons.Rounded.Info, contentDescription = null) },
        trailingContent = {
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null
            )
        },
        modifier = Modifier.clickable {
            onAboutClick()
        }
    )
}}

        
    }
}