# Compose UI Module

This module contains the Compose Multiplatform implementation of the AndLogView user interface.

## Overview

The Compose UI module provides a modern, declarative UI implementation using Jetpack Compose for Desktop. This refactoring aims to replace the existing Swing-based UI with a more maintainable and modern UI framework.

## Architecture

### Main Components

- **MainWindow** (`MainWindow.kt`): The main application window composable
  - Uses Material 3 design system
  - Provides the top-level application structure
  - Manages the overall application state

- **LogTable** (`components/LogTable.kt`): Displays log records in a scrollable table
  - Efficient lazy loading for large log files
  - Color-coded log priorities (Verbose, Debug, Info, Warning, Error, Fatal)
  - Monospace font for better readability
  - Columnar layout: Time, Priority, Tag, PID, Message

### Design Decisions

1. **Material 3 Design**: Using Material 3 for modern, consistent UI components
2. **Declarative UI**: Compose's declarative approach simplifies UI state management
3. **Lazy Loading**: LazyColumn for efficient rendering of large log lists
4. **Type Safety**: Kotlin data classes for type-safe log record representation

## Building and Running

To build the Compose UI module:

```bash
./gradlew :composeui:build
```

To run the standalone Compose application:

```bash
./gradlew :composeui:run
```

## Integration with Existing Code

The Compose UI is designed to coexist with the existing Swing UI during the transition period. The integration strategy includes:

1. **Bridge Pattern**: Adapters to connect existing Java business logic with Compose UI
2. **Dependency Injection**: Using Dagger for dependency management
3. **Gradual Migration**: Components can be migrated incrementally

## Dependencies

- **Compose Multiplatform**: JetBrains Compose for Desktop
- **Material 3**: Material Design 3 components
- **Kotlin**: Kotlin 2.1.0 with Compose compiler plugin

## Future Work

- [ ] Add search functionality UI
- [ ] Implement filter panels
- [ ] Add bookmarks UI
- [ ] Device connection UI
- [ ] Settings/preferences dialog
- [ ] Complete integration with existing business logic
- [ ] Performance optimization for very large log files
- [ ] Add keyboard shortcuts
- [ ] Implement context menus
- [ ] Add theming support

## Development Guidelines

1. **Composable Functions**: Keep composables small and focused
2. **State Management**: Use `remember` and `mutableStateOf` for local state
3. **Performance**: Use `derivedStateOf` and `LaunchedEffect` appropriately
4. **Naming**: Follow Compose naming conventions (PascalCase for composables)
5. **Testing**: Write UI tests using Compose testing framework

## References

- [Compose Multiplatform Documentation](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Material 3 Design](https://m3.material.io/)
- [Compose Desktop Documentation](https://github.com/JetBrains/compose-multiplatform)
