package com.cutex.crashscope.ui.permission

import android.Manifest
import android.app.AppOpsManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

@Composable
fun PermissionScreen(
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val pkg = context.packageName
    
    
    val commands = listOf(
    "adb shell pm grant $pkg android.permission.READ_LOGS",
    "adb shell pm grant $pkg android.permission.READ_DROPBOX_DATA",
    "adb shell appops set $pkg GET_USAGE_STATS allow" 
)


    
    fun verifyAndProceed() {
        
        val hasLogs = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_LOGS) == PackageManager.PERMISSION_GRANTED
        
        
        val hasDropbox = context.checkPermission("android.permission.READ_DROPBOX_DATA", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED
        
        
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), pkg)
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), pkg)
        }
        val hasUsageStats = mode == AppOpsManager.MODE_ALLOWED

        
        if (hasLogs && hasDropbox && hasUsageStats) {
            onPermissionGranted()
        } else {
            val missing = mutableListOf<String>()
            if (!hasLogs) missing.add("READ_LOGS")
            if (!hasDropbox) missing.add("READ_DROPBOX_DATA")
            if (!hasUsageStats) missing.add("PACKAGE_USAGE_STATS")
            
            Toast.makeText(context, "Missing: ${missing.joinToString(", ")}", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            Button(
                onClick = { verifyAndProceed() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("I Have Run These Commands", fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            
            Icon(
                imageVector = Icons.Rounded.Terminal,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "System Access",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "CrashScope requires system access to function. These permissions must be granted manually via ADB.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            
            ElevatedCard(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Terminal,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Execute Terminal Commands",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    
                    commands.forEachIndexed { index, cmd ->
                        TerminalBox(step = index + 1, command = cmd, context = context)
                        if (index < commands.size - 1) {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            
            Text(
                text = "Execution Guide",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            GuideItem(
                title = "1: PC Setup",
                description = "Connect phone via USB debugging and run commands in ADB terminal."
            )
            
            GuideItem(
                title = "2: On-Device Tools",
                description = "Use aShell (via Shizuku) or LADB to run these directly on your phone."
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TerminalBox(step: Int, command: String, context: Context) {
    Column {
        Text(
            text = "Command #$step",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
        ) {
            Row(
                modifier = Modifier.padding(start = 12.dp, top = 4.dp, bottom = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = command,
                    modifier = Modifier.weight(1f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                
                IconButton(onClick = {
                    val cb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cb.setPrimaryClip(ClipData.newPlainText("ADB", command))
                    Toast.makeText(context, "Command $step copied", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun GuideItem(title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Rounded.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(20.dp).padding(top = 2.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
