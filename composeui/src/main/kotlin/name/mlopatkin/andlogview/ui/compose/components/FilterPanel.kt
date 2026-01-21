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

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Filter panel for configuring log filtering options
 */
@Composable
fun FilterPanel(
    selectedPriorities: Set<String>,
    onPriorityToggle: (String) -> Unit,
    tagFilter: String,
    onTagFilterChange: (String) -> Unit,
    pidFilter: String,
    onPidFilterChange: (String) -> Unit,
    messageFilter: String,
    onMessageFilterChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Filters",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Priority filters
            Text(
                text = "Priority:",
                style = MaterialTheme.typography.labelMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val priorities = listOf("V", "D", "I", "W", "E", "F")
                priorities.forEach { priority ->
                    FilterChip(
                        selected = selectedPriorities.contains(priority),
                        onClick = { onPriorityToggle(priority) },
                        label = { Text(priority) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Tag filter
            OutlinedTextField(
                value = tagFilter,
                onValueChange = onTagFilterChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Tag") },
                placeholder = { Text("Filter by tag") },
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // PID filter
            OutlinedTextField(
                value = pidFilter,
                onValueChange = onPidFilterChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("PID") },
                placeholder = { Text("Filter by process ID") },
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Message filter
            OutlinedTextField(
                value = messageFilter,
                onValueChange = onMessageFilterChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Message") },
                placeholder = { Text("Filter by message content") },
                singleLine = true
            )
        }
    }
}
