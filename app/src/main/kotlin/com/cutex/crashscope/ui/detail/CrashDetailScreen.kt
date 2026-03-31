package com.cutex.crashscope.ui.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cutex.crashscope.data.model.CrashEvent
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrashDetailScreen(
    crash: CrashEvent,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    
    var appName by remember { mutableStateOf(crash.packageName) }
    
    
    LaunchedEffect(crash.packageName) {
        try {
            val pm = context.packageManager
            val appInfo = pm.getApplicationInfo(crash.packageName, 0)
            appName = pm.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            appName = crash.packageName 
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    
                    IconButton(onClick = { copyToClipboard(context, crash.stackTrace) }) {
                        Icon(Icons.Rounded.ContentCopy, contentDescription = "Copy")
                    }
                    
                    IconButton(onClick = { shareCrashReport(context, crash, appName) }) {
                        Icon(Icons.Rounded.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            
            ElevatedCard(
                shape = RoundedCornerShape(24.dp), 
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        
                        val fallbackPainter = rememberAsyncImagePainter(
                            model = android.R.drawable.sym_def_app_icon
                        )

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(context.packageManager.getApplicationIconOrNull(crash.packageName))
                                .crossfade(true)
                                .build(),
                            contentDescription = "App Icon",
                            modifier = Modifier.size(48.dp),
                            error = fallbackPainter,
                            placeholder = fallbackPainter
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column {
                            Text(
                                text = appName,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = crash.packageName,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DeviceInfoItem(
                            icon = Icons.Rounded.Smartphone,
                            label = "Device",
                            value = "${Build.MANUFACTURER} ${Build.MODEL}"
                        )
                        DeviceInfoItem(
                            icon = Icons.Rounded.Android,
                            label = "Android",
                            value = "${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})"
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                     
                    Text(
                        text = "Time: " + formatDate(crash.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                shape = RoundedCornerShape(24.dp), 
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Rounded.Warning, 
                        contentDescription = null, 
                        tint = MaterialTheme.colorScheme.onErrorContainer 
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Exception",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                        )
                        Text(
                            text = crash.exceptionType,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            Text(
                text = "Stack Trace",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )

            
            SelectionContainer {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp)) 
                        .background(Color(0xFF1E1E1E)) 
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp)) 
                        .padding(16.dp)
                ) {
                    Text(
                        text = crash.stackTrace,
                        color = Color(0xFFA5D6A7),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}



fun PackageManager.getApplicationIconOrNull(packageName: String): android.graphics.drawable.Drawable? {
    return try {
        getApplicationIcon(packageName)
    } catch (e: Exception) {
        null
    }
}

@Composable
fun DeviceInfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Crash Log", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Log copied to clipboard", Toast.LENGTH_SHORT).show()
}

private fun shareCrashReport(context: Context, crash: CrashEvent, appName: String) {
    val report = """
        Application: $appName
        Package: ${crash.packageName}
        Date: ${formatDate(crash.timestamp)}
        Device: ${Build.MANUFACTURER} ${Build.MODEL} (Android ${Build.VERSION.RELEASE})
        
        Exception: ${crash.exceptionType}
        
        Stack Trace:
        ${crash.stackTrace}
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Crash Report: $appName")
        putExtra(Intent.EXTRA_TEXT, report)
    }
    
    try {
        context.startActivity(Intent.createChooser(intent, "Share Crash Report"))
    } catch (e: Exception) {
        Toast.makeText(context, "No app found to share", Toast.LENGTH_SHORT).show()
    }
}

private fun formatDate(timestamp: Long): String {
    
    return SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(timestamp))
}
