/*
 * Copyright 2026 The AndLogView authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package name.mlopatkin.andlogview.ui.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Log record data class for display
 */
data class LogRecordItem(
    val timestamp: String,
    val priority: String,
    val tag: String,
    val pid: Int,
    val message: String
)

/**
 * Log table component that displays log records in a scrollable list
 */
@Composable
fun LogTable(
    logRecords: List<LogRecordItem>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            // Header
            LogTableHeader()
            
            // Log records
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(logRecords) { record ->
                    LogTableRow(record)
                    Divider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
private fun LogTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
    ) {
        Text(
            text = "Time",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.weight(0.15f)
        )
        Text(
            text = "Priority",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.weight(0.08f)
        )
        Text(
            text = "Tag",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.weight(0.15f)
        )
        Text(
            text = "PID",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.weight(0.08f)
        )
        Text(
            text = "Message",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.weight(0.54f)
        )
    }
}

@Composable
private fun LogTableRow(record: LogRecordItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = record.timestamp,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(0.15f)
        )
        Text(
            text = record.priority,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = getPriorityColor(record.priority),
            modifier = Modifier.weight(0.08f)
        )
        Text(
            text = record.tag,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(0.15f)
        )
        Text(
            text = record.pid.toString(),
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(0.08f)
        )
        Text(
            text = record.message,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(0.54f)
        )
    }
}

private fun getPriorityColor(priority: String): Color {
    return when (priority) {
        "V" -> Color(0xFF808080) // Verbose - Gray
        "D" -> Color(0xFF0000FF) // Debug - Blue
        "I" -> Color(0xFF008000) // Info - Green
        "W" -> Color(0xFFFFA500) // Warning - Orange
        "E" -> Color(0xFFFF0000) // Error - Red
        "F" -> Color(0xFF8B0000) // Fatal - Dark Red
        else -> Color.Black
    }
}
