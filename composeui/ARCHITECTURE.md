# Architecture Overview

This document describes the architecture of the Compose Multiplatform refactoring.

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         AndLogView Application                   │
└─────────────────────────────────────────────────────────────────┘
                                  │
                    ┌─────────────┴─────────────┐
                    │                           │
         ┌──────────▼──────────┐    ┌──────────▼──────────┐
         │   Swing UI (Legacy) │    │   Compose UI (New)  │
         │    (Java/Swing)     │    │  (Kotlin/Compose)   │
         └──────────┬──────────┘    └──────────┬──────────┘
                    │                           │
                    └─────────────┬─────────────┘
                                  │
                    ┌─────────────▼─────────────┐
                    │      Adapter Layer        │
                    │  (Kotlin/Java Interop)    │
                    └─────────────┬─────────────┘
                                  │
                    ┌─────────────▼─────────────┐
                    │   Business Logic Layer    │
                    │  - LogModel               │
                    │  - FilterModel            │
                    │  - BookmarkModel          │
                    │  - SearchModel            │
                    │  - DeviceModel            │
                    └─────────────┬─────────────┘
                                  │
                    ┌─────────────▼─────────────┐
                    │      Data Layer           │
                    │  - File I/O               │
                    │  - ADB Communication      │
                    │  - Log Parsing            │
                    └───────────────────────────┘
```

## Compose UI Module Structure

```
composeui/
├── src/main/kotlin/name/mlopatkin/andlogview/ui/compose/
│   ├── MainWindow.kt              # Main application window
│   ├── components/
│   │   ├── LogTable.kt            # Log display table
│   │   ├── SearchBar.kt           # Search functionality
│   │   ├── FilterPanel.kt         # Filter controls
│   │   ├── MenuBar.kt             # Application menu
│   │   └── [More components...]   # Additional UI components
│   ├── viewmodels/
│   │   ├── LogTableViewModel.kt   # State management for log table
│   │   ├── FilterViewModel.kt     # State management for filters
│   │   └── [More viewmodels...]   # Additional view models
│   └── adapters/
│       ├── LogModelAdapter.kt     # Bridges Java LogModel to Compose
│       ├── FilterModelAdapter.kt  # Bridges Java FilterModel to Compose
│       └── [More adapters...]     # Additional adapters
├── README.md                       # Module documentation
└── INTEGRATION.md                  # Integration guide
```

## Data Flow

### Log Display Flow

```
┌──────────────┐
│  Log Source  │ (File/Device)
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  Log Parser  │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  Log Model   │ (Business Logic)
└──────┬───────┘
       │
       ▼
┌──────────────┐
│    Adapter   │ (LogModelAdapter)
└──────┬───────┘
       │
       ▼ StateFlow
┌──────────────┐
│  ViewModel   │ (Compose State)
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  LogTable    │ (Compose UI)
└──────────────┘
```

### Filter Flow

```
User Input (UI)
      │
      ▼
FilterPanel (Compose)
      │
      ▼
FilterViewModel
      │
      ▼
FilterModelAdapter
      │
      ▼
FilterModel (Java)
      │
      ▼
Log Filtering Logic
      │
      ▼
Filtered Results
      │
      ▼
LogTable Update
```

## Component Communication

### State Management Pattern

```kotlin
// Business Logic (Java)
class LogModel {
    private val listeners = mutableListOf<LogRecordListener>()
    
    fun addRecord(record: LogRecord) {
        // Add record
        notifyListeners(record)
    }
}

// Adapter Layer (Kotlin)
class LogModelAdapter(private val logModel: LogModel) {
    private val _logs = MutableStateFlow<List<LogRecordItem>>(emptyList())
    val logs: StateFlow<List<LogRecordItem>> = _logs.asStateFlow()
    
    init {
        logModel.addListener { record ->
            _logs.value = _logs.value + record.toLogRecordItem()
        }
    }
}

// UI Layer (Compose)
@Composable
fun LogTable(adapter: LogModelAdapter) {
    val logs by adapter.logs.collectAsState()
    
    LazyColumn {
        items(logs) { log ->
            LogTableRow(log)
        }
    }
}
```

## Technology Stack

### Frontend (Compose UI)
- **Compose Multiplatform**: Desktop UI framework
- **Material 3**: Design system
- **Kotlin Coroutines**: Async programming
- **StateFlow**: Reactive state management

### Backend (Business Logic)
- **Java 17+**: Core language
- **Guava**: Utilities
- **Dagger**: Dependency injection
- **SLF4J/Log4j**: Logging

### Integration
- **Kotlin-Java Interop**: Seamless integration
- **Flow/Observable Adapters**: Bridge reactive systems
- **Dagger Modules**: Shared dependency injection

## Design Principles

1. **Separation of Concerns**: UI, business logic, and data layers are separate
2. **Reactive Architecture**: State flows from data layer to UI
3. **Testability**: Each layer can be tested independently
4. **Gradual Migration**: Old and new UI can coexist
5. **Type Safety**: Kotlin's type system prevents many bugs
6. **Immutability**: Prefer immutable data structures

## Performance Considerations

- **Lazy Loading**: Only render visible log entries
- **Virtualization**: Compose automatically virtualizes lists
- **State Hoisting**: Keep state at the appropriate level
- **Efficient Recomposition**: Use `remember` and `derivedStateOf`
- **Background Processing**: Use coroutines for heavy work

## Future Enhancements

- Context menus with right-click actions
- Drag and drop file opening
- Multiple log window support
- Custom themes and color schemes
- Keyboard shortcut customization
- Export to various formats
- Advanced regex search with highlighting
- Performance profiling and optimization
