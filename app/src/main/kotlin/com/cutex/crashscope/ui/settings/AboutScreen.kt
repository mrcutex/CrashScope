package com.cutex.crashscope.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cutex.crashscope.BuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
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
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "CrashScope",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "View and understand crash and ANR reports directly on your device.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                ElevatedCard {
                    Column {
                        ListItem(
                            headlineContent = { Text("GitHub") },
                            supportingContent = { Text("View project repository") },
                            leadingContent = {
                                Icon(Icons.Rounded.Code, contentDescription = null)
                            },
                            trailingContent = {
                                Icon(Icons.Rounded.ChevronRight, null)
                            },
                            modifier = Modifier.clickable {
                                uriHandler.openUri("https://github.com/mrcutex/CrashScope")
                            }
                        )

                        HorizontalDivider()

                        ListItem(
                            headlineContent = { Text("License") },
                            supportingContent = { Text("MIT License") },
                            leadingContent = {
                                Icon(Icons.Rounded.Security, null)
                            }
                        )

                        HorizontalDivider()

                        ListItem(
                            headlineContent = { Text("Version") },
                            supportingContent = { Text(BuildConfig.VERSION_NAME) },
                            leadingContent = {
                                Icon(Icons.Rounded.Info, null)
                            }
                        )

                        HorizontalDivider()

                        ListItem(
                            headlineContent = { Text("Developer") },
                            supportingContent = { Text("Cutex") },
                            leadingContent = {
                                Icon(Icons.Rounded.Person, null)
                            }
                        )
                    }
                }
            }
        }
    }
}