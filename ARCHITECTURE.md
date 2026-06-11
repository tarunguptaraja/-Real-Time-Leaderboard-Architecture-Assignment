# Architecture Design & Technical Decisions

This document outlines the architectural decisions, performance strategy, scalability choices, and code review guidelines for the real-time leaderboard project.

---

## Modularity & Module Boundaries

The project is split into three Gradle modules: `:engine`, `:domain`, and `:app`.

* **Separation of Concerns**: `:engine` simulates the gameplay, while `:domain` calculates ranking state. Neither module has dependencies on Android, which allows testing them with plain JUnit without Robolectric.
* **Koin DI**: We chose Koin because it is lightweight, Kotlin-first, and avoids code generation overhead (unlike Hilt), keeping compilation times low.

---

## UI Framework: Jetpack Compose vs XML Layouts

We chose **Jetpack Compose** over XML layouts for this project due to the following engineering considerations:

1. **Declarative State Mapping**: Compose allows binding the `StateFlow<List<LeaderboardEntry>>` directly using `collectAsStateWithLifecycle()`. The UI automatically reacts to state changes, eliminating the need for `DiffUtil`, custom `ListAdapter` binding boilerplate, and manual view updates.
2. **Simplified Motion Animations**: Handling row reordering and score-change flashes is straightforward. Using `Modifier.animateItem()` on the `LazyColumn` handles item translation animations out-of-the-box. Implementing equivalent transitions in an XML `RecyclerView` requires complex custom layout animations.
3. **Elimination of State Desynchronization**: In the XML View system, widgets maintain their own internal state (e.g., text values, visibility, scroll offsets), requiring manual synchronization with business logic state. This is highly error-prone and leads to flickering in rapid real-time updates. Compose solves this by treating the UI as a pure function of the immutable state.

---

## Performance and Lifecycle Strategy

### 1. Main Thread Isolation
All score event generation and ranking computations run on `Dispatchers.Default`. The main thread is reserved strictly for Compose rendering and layout animations.

### 2. Memory & Recomposition Optimization
* **Stable Keys**: `LazyColumn` items are keyed on `playerId` rather than list index. This ensures Compose reorders rows via animations without redrawing or recreating unchanged cards.
* **Immutable State**: State data structures use standard immutable Kotlin data classes so Compose can skip recompositions on unmodified data.
* **Lifecycle Awareness**: Flow collection uses `collectAsStateWithLifecycle()`. If the app goes into the background, updates stop consuming CPU resources.
* **Scope Control**: Background processing is scoped to `viewModelScope`, ensuring all coroutines are cancelled when the ViewModel is cleared.

### 3. Scaling to 100K Users
Sorting a list of 100K items on every single score event on-device would block the CPU. To scale:
1. **Windowing**: Only load the top 100 players and a small window around the active player's rank.
2. **Data Structure Change**: Swap the flat list sorting for a self-balancing binary search tree or skip list to reduce insertion complexity to `O(log N)`.
3. **Batching**: Group score updates and compute them in chunks (e.g. once per second) rather than processing each event instantly.
4. **Server-side Calculation**: Offload sorting to a server database (e.g., Redis Sorted Sets) and push changes to the client via WebSockets.

---

## Simulated Code Review Feedback

Below is representative feedback for a mid-level engineer's initial implementation:

1. **[Critical] Extract ranking from UI/ViewModel**
   > *Feedback*: Do not calculate ranks or sort items inside the ViewModel or Repository. This is business logic and belongs in the `:domain` layer (e.g., inside `RankingEngine`).
2. **[Critical] Fix dense ranking tie-handling**
   > *Feedback*: The ranking loop currently assigns sequential ranks. The rules require dense ranking: if two players tie at rank 2, the next player must be rank 4, not 3.
3. **[Critical] Enforce score bounds**
   > *Feedback*: The generator is accepting negative values. Add validation to reject increments `<= 0` since scores must only increase.
4. **[Improvement] Set stable LazyColumn keys**
   > *Feedback*: Use the player's unique ID for the `LazyColumn` item keys instead of the list index. This allows smooth reorder animations.
5. **[Improvement] Decouple diagnostic logs**
   > *Feedback*: Avoid calling `println` directly in the generator. Pass an optional logging lambda through the constructor so we can disable console noise in tests.
6. **[Improvement] Collect flow with lifecycle awareness**
   > *Feedback*: Swap `collectAsState` for `collectAsStateWithLifecycle` to avoid background battery drain.

---

## 7-Day Shipping Plan & Ownership

For a 7-day launch window with a Lead (myself), Mid-Level, and Junior dev:

### Non-Negotiables (Must Ship)
* Complete `:domain` ranking logic with full unit test coverage.
* Decoupled generator emitting deterministic event sequences.
* Core scrollable UI showing ranks, ties, and position movements.

### Cut / Defer Items
* Local database persistence (Room).
* Search bar and filtering.
* Decorative UI particle effects.

### Team Allocation
* **Junior**: UI row design, text styling, and rank badge color configurations.
* **Mid-Level**: Engine implementation, Koin module declarations, and ViewModel coordination.
* **Lead (Myself)**: Gradle module layout setup, core ranking algorithms, and unit test coverage.
