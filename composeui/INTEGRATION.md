# Integration Guide: Compose UI with Existing Java Code

This document describes how to integrate the new Compose Multiplatform UI with the existing Java/Swing codebase.

## Overview

The Compose UI module is designed to coexist with the existing Swing UI during the migration period. This allows for gradual refactoring while maintaining a working application.

## Integration Strategies

### 1. Adapter Pattern

Create adapter classes that bridge between Java business logic and Compose UI state:

```kotlin
// Example: LogModelAdapter.kt
class LogModelAdapter(private val logModel: LogModel) {
    val logsFlow: StateFlow<List<LogRecordItem>> = logModel
        .observeLogs()
        .map { it.toLogRecordItem() }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())
}

private fun LogRecord.toLogRecordItem() = LogRecordItem(
    timestamp = timestamp.toString(),
    priority = priority.letter,
    tag = tag,
    pid = pid,
    message = message
)
```

### 2. State Management

Use Kotlin Flow and StateFlow to manage state between Java and Compose:

```kotlin
// Example: FilterStateManager.kt
class FilterStateManager(
    private val filterModel: FilterModel
) {
    private val _activeFilters = MutableStateFlow<Set<Filter>>(emptySet())
    val activeFilters: StateFlow<Set<Filter>> = _activeFilters.asStateFlow()
    
    fun addFilter(filter: Filter) {
        filterModel.addFilter(filter)  // Call Java API
        _activeFilters.value += filter
    }
    
    fun removeFilter(filter: Filter) {
        filterModel.removeFilter(filter)  // Call Java API
        _activeFilters.value -= filter
    }
}
```

### 3. Dependency Injection

Use Dagger to inject dependencies into Compose screens:

```kotlin
// Example: ComposeMainModule.kt
@Module
class ComposeMainModule {
    @Provides
    fun provideLogModelAdapter(logModel: LogModel): LogModelAdapter {
        return LogModelAdapter(logModel)
    }
    
    @Provides
    fun provideFilterStateManager(filterModel: FilterModel): FilterStateManager {
        return FilterStateManager(filterModel)
    }
}
```

### 4. Event Handling

Handle UI events and dispatch them to Java business logic:

```kotlin
// Example: LogTableViewModel.kt
class LogTableViewModel @Inject constructor(
    private val logModel: LogModel,
    private val filterModel: FilterModel
) {
    private val _selectedRecords = MutableStateFlow<List<LogRecordItem>>(emptyList())
    val selectedRecords: StateFlow<List<LogRecordItem>> = _selectedRecords
    
    fun onRecordSelected(record: LogRecordItem) {
        // Handle selection
    }
    
    fun onCopyToClipboard(records: List<LogRecordItem>) {
        // Call Java clipboard API
    }
}
```

## Migration Steps

### Phase 1: Setup and Infrastructure
1. [x] Create composeui module
2. [x] Add Compose dependencies
3. [x] Create basic UI components
4. [ ] Setup dependency injection for Compose

### Phase 2: Adapter Layer
1. [ ] Create LogModelAdapter
2. [ ] Create FilterModelAdapter
3. [ ] Create BookmarkModelAdapter
4. [ ] Create SearchAdapter
5. [ ] Create DeviceModelAdapter

### Phase 3: Main Window Integration
1. [ ] Create ComposeMainWindow
2. [ ] Wire up LogTable with real data
3. [ ] Integrate search functionality
4. [ ] Integrate filter functionality
5. [ ] Add menu bar actions

### Phase 4: Feature Parity
1. [ ] Port all Swing features to Compose
2. [ ] Add keyboard shortcuts
3. [ ] Add context menus
4. [ ] Add drag and drop support
5. [ ] Add window positioning persistence

### Phase 5: Transition
1. [ ] Add feature flag to switch between UIs
2. [ ] Run both UIs in parallel for testing
3. [ ] Deprecate Swing UI
4. [ ] Remove Swing dependencies

## Code Examples

### Launching Compose from Java Main

```java
// In Main.java
public class Main {
    public static void main(String[] args) {
        // Initialize DI
        AppGlobals globals = DaggerAppGlobals.factory().create(...);
        
        // Check for Compose UI flag
        if (commandLine.useComposeUi()) {
            MainWindowKt.launchComposeApp(globals);
        } else {
            // Launch Swing UI
            EventQueue.invokeLater(() -> globals.getMain().start());
        }
    }
}
```

### Observing Java Observable in Compose

```kotlin
@Composable
fun ObserveJavaObservable(
    observable: Observable<LogRecord>
) {
    val records by observable
        .asFlow()
        .collectAsState(initial = emptyList())
    
    LogTable(logRecords = records)
}

// Extension function
fun <T> Observable<T>.asFlow(): Flow<T> = callbackFlow {
    val listener = Observer<T> { trySend(it) }
    addObserver(listener)
    awaitClose { removeObserver(listener) }
}
```

## Testing Strategy

### Unit Tests
```kotlin
@Test
fun testLogTableFiltering() {
    val logs = listOf(
        LogRecordItem("...", "E", "Tag1", 1, "Error message"),
        LogRecordItem("...", "I", "Tag2", 2, "Info message")
    )
    
    // Test filtering logic
    val filtered = logs.filter { it.priority == "E" }
    assertEquals(1, filtered.size)
}
```

### UI Tests
```kotlin
@OptIn(ExperimentalTestApi::class)
@Test
fun testMainWindowRendering() = runComposeUiTest {
    setContent {
        MainWindow()
    }
    
    // Verify components are rendered
    onNodeWithText("AndLogView").assertExists()
    onNodeWithText("Search").assertExists()
}
```

## Performance Considerations

1. **Lazy Loading**: Use LazyColumn for large datasets
2. **Virtualization**: Compose automatically virtualizes off-screen items
3. **State Hoisting**: Keep state at the appropriate level
4. **Recomposition**: Use `remember` and `derivedStateOf` to minimize recomposition
5. **Background Processing**: Use coroutines for heavy computations

## Best Practices

1. **Separation of Concerns**: Keep business logic in Java, UI in Compose
2. **Testability**: Write unit tests for adapters and ViewModels
3. **Type Safety**: Use data classes for UI state
4. **Immutability**: Prefer immutable data structures
5. **Documentation**: Document integration points clearly

## Troubleshooting

### Common Issues

1. **ClassNotFoundException**: Ensure all dependencies are properly configured
2. **State Not Updating**: Check StateFlow collection and emission
3. **Memory Leaks**: Properly cancel coroutines and flows
4. **Threading Issues**: Use appropriate dispatchers for UI and background work

## Resources

- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Compose State Management](https://developer.android.com/jetpack/compose/state)
- [Java-Kotlin Interop](https://kotlinlang.org/docs/java-interop.html)
