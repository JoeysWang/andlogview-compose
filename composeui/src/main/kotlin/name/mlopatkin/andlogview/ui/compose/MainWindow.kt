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
package name.mlopatkin.andlogview.ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import name.mlopatkin.andlogview.ui.compose.components.*

/**
 * Main window for AndLogView using Compose Multiplatform
 */
@Composable
@Preview
fun MainWindow(
    onCloseRequest: () -> Unit = {}
) {
    // Sample log records for demonstration
    val allLogs = remember {
        listOf(
            LogRecordItem("2026-01-21 12:00:01.123", "I", "System", 1234, "Application started"),
            LogRecordItem("2026-01-21 12:00:01.456", "D", "ActivityManager", 1234, "Activity resumed"),
            LogRecordItem("2026-01-21 12:00:02.789", "W", "NetworkManager", 2345, "Connection timeout"),
            LogRecordItem("2026-01-21 12:00:03.012", "E", "DatabaseHelper", 1234, "Failed to open database"),
            LogRecordItem("2026-01-21 12:00:03.345", "V", "LocationService", 3456, "Location update received"),
            LogRecordItem("2026-01-21 12:00:04.567", "I", "NetworkManager", 2345, "Connection established"),
            LogRecordItem("2026-01-21 12:00:05.890", "D", "DatabaseHelper", 1234, "Database query executed"),
            LogRecordItem("2026-01-21 12:00:06.123", "W", "System", 1234, "Low memory warning"),
        )
    }
    
    // Filter state
    var searchQuery by remember { mutableStateOf("") }
    var selectedPriorities by remember { mutableStateOf(setOf("V", "D", "I", "W", "E", "F")) }
    var tagFilter by remember { mutableStateOf("") }
    var pidFilter by remember { mutableStateOf("") }
    var messageFilter by remember { mutableStateOf("") }
    var showFilterPanel by remember { mutableStateOf(false) }
    
    // Filtered logs based on current filters
    val filteredLogs = remember(allLogs, selectedPriorities, tagFilter, pidFilter, messageFilter, searchQuery) {
        allLogs.filter { log ->
            val priorityMatch = selectedPriorities.contains(log.priority)
            val tagMatch = tagFilter.isEmpty() || log.tag.contains(tagFilter, ignoreCase = true)
            val pidMatch = pidFilter.isEmpty() || log.pid.toString().contains(pidFilter)
            val messageMatch = messageFilter.isEmpty() || log.message.contains(messageFilter, ignoreCase = true)
            val searchMatch = searchQuery.isEmpty() || 
                log.tag.contains(searchQuery, ignoreCase = true) ||
                log.message.contains(searchQuery, ignoreCase = true)
            
            priorityMatch && tagMatch && pidMatch && messageMatch && searchMatch
        }
    }
    
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AndLogView - Compose Multiplatform") },
                    actions = {
                        TextButton(onClick = { showFilterPanel = !showFilterPanel }) {
                            Text(if (showFilterPanel) "Hide Filters" else "Show Filters")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Search bar
                SearchBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    onSearch = { /* Search is reactive, no action needed */ },
                    onClear = { searchQuery = "" }
                )
                
                // Filter panel (collapsible)
                if (showFilterPanel) {
                    FilterPanel(
                        selectedPriorities = selectedPriorities,
                        onPriorityToggle = { priority ->
                            selectedPriorities = if (selectedPriorities.contains(priority)) {
                                selectedPriorities - priority
                            } else {
                                selectedPriorities + priority
                            }
                        },
                        tagFilter = tagFilter,
                        onTagFilterChange = { tagFilter = it },
                        pidFilter = pidFilter,
                        onPidFilterChange = { pidFilter = it },
                        messageFilter = messageFilter,
                        onMessageFilterChange = { messageFilter = it }
                    )
                }
                
                // Status bar showing filter count
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 1.dp
                ) {
                    Text(
                        text = "Showing ${filteredLogs.size} of ${allLogs.size} log entries",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                
                // Log table
                LogTable(
                    logRecords = filteredLogs,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

/**
 * Launch the Compose application
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AndLogView",
        state = rememberWindowState(width = 1200.dp, height = 800.dp)
    ) {
        MainWindow(onCloseRequest = ::exitApplication)
    }
}
