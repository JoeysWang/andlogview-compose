/*
 * Copyright 2025 The AndLogView authors
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

import androidx.compose.runtime.*

/**
 * Menu item data class for dynamic menu construction
 */
data class MenuItem(
    val label: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val shortcut: String? = null,
    val submenu: List<MenuItem>? = null
)

/**
 * Helper function to create menu structure for AndLogView
 */
fun createMenuStructure(
    onOpenFile: () -> Unit,
    onConnectDevice: () -> Unit,
    onSaveLog: () -> Unit,
    onExit: () -> Unit,
    onShowPreferences: () -> Unit,
    onShowAbout: () -> Unit,
    onToggleAutoScroll: () -> Unit,
    onClearLog: () -> Unit,
): List<MenuItem> {
    return listOf(
        MenuItem(
            label = "File",
            onClick = {},
            submenu = listOf(
                MenuItem("Open File...", onOpenFile, shortcut = "Ctrl+O"),
                MenuItem("Connect to Device...", onConnectDevice, shortcut = "Ctrl+D"),
                MenuItem("Save Log...", onSaveLog, shortcut = "Ctrl+S"),
                MenuItem("Exit", onExit, shortcut = "Ctrl+Q")
            )
        ),
        MenuItem(
            label = "Edit",
            onClick = {},
            submenu = listOf(
                MenuItem("Copy", {}, shortcut = "Ctrl+C"),
                MenuItem("Select All", {}, shortcut = "Ctrl+A"),
                MenuItem("Find...", {}, shortcut = "Ctrl+F")
            )
        ),
        MenuItem(
            label = "View",
            onClick = {},
            submenu = listOf(
                MenuItem("Auto Scroll", onToggleAutoScroll),
                MenuItem("Show Filters", {}),
                MenuItem("Clear Log", onClearLog)
            )
        ),
        MenuItem(
            label = "Tools",
            onClick = {},
            submenu = listOf(
                MenuItem("Preferences...", onShowPreferences)
            )
        ),
        MenuItem(
            label = "Help",
            onClick = {},
            submenu = listOf(
                MenuItem("About", onShowAbout)
            )
        )
    )
}
