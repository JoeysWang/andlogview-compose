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
import name.mlopatkin.andlogview.ui.compose.components.LogRecordItem
import name.mlopatkin.andlogview.ui.compose.components.LogTable

/**
 * Main window for AndLogView using Compose Multiplatform
 */
@Composable
@Preview
fun MainWindow(
    onCloseRequest: () -> Unit = {}
) {
    // Sample log records for demonstration
    val sampleLogs = remember {
        listOf(
            LogRecordItem("2026-01-21 12:00:01.123", "I", "System", 1234, "Application started"),
            LogRecordItem("2026-01-21 12:00:01.456", "D", "ActivityManager", 1234, "Activity resumed"),
            LogRecordItem("2026-01-21 12:00:02.789", "W", "NetworkManager", 2345, "Connection timeout"),
            LogRecordItem("2026-01-21 12:00:03.012", "E", "DatabaseHelper", 1234, "Failed to open database"),
            LogRecordItem("2026-01-21 12:00:03.345", "V", "LocationService", 3456, "Location update received"),
        )
    }
    
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AndLogView - Compose Multiplatform") }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LogTable(
                    logRecords = sampleLogs,
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
