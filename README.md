# Real-Time Leaderboard

This project is a real-time leaderboard system built for Android using Jetpack Compose, Kotlin Coroutines/Flow, and Koin.

The codebase is split into three Gradle modules to enforce compile-time boundaries between gameplay event generation, ranking logic, and the presentation layer.

---

## How to Run the Project

### Requirements
* JDK 17
* Android SDK 34+

### Run Unit Tests
To execute JVM unit tests across all modules:
```bash
./gradlew test
```

### Build Debug APK
To build the debug package:
```bash
./gradlew assembleDebug
```
---

## Module Responsibilities

* **`:engine`**: A pure Kotlin/JVM module. It maintains the list of players and emits random score updates at intervals of 500ms–2000ms. It has no dependencies on other modules.
* **`:domain`**: A pure Kotlin/JVM module. It processes score updates to calculate dense ranks (ties share ranks, rank numbers skip after ties), manages position movements, and exposes the repository. It depends on `:engine` only for shared data models.
* **`:app`**: The Android application module. It manages the Jetpack Compose UI, theme configurations, the `LeaderboardViewModel`, and wires everything together using Koin.

---

## Architecture Overview

The application follows Clean Architecture principles combined with MVVM:

```
┌──────────────────────────────────────────┐
│                   :app                   │
│   (Android UI, ViewModel, Koin Config)   │
└────────────┬────────────────────┬────────┘
             │                    │
             ▼                    ▼
     ┌──────────────┐      ┌──────────────┐
     │   :domain    │      │   :engine    │
     │(Ranking Core)│ ───> │(Event Source)│
     └──────────────┘      └──────────────┘
```

* **`:app`** handles presentation, collecting leaderboard updates from the repository and rendering them in the Compose UI.
* **`:domain`** manages the business rules. It exposes the ranking engine contract and maintains the sorted state.
* **`:engine`** acts as the data source, simulating game events and player records.

---

## Trade-offs Made

1. **MVVM instead of MVI**
   For a simple single-screen leaderboard, MVI adds unnecessary boilerplate (sealed event/state/effect structures). MVVM keeps the implementation lightweight and focused.
2. **Koin instead of Hilt**
   Koin is lightweight, Kotlin-first, and does not require annotation processors or code generation (APT/KSP). This keeps Gradle build times fast.
3. **Seeded Deterministic Random**
   The engine uses a seeded random generator. While a live game would use dynamic streams, a seeded approach ensures session predictability, making debugging and testing reliable.
4. **On-Device Sorting**
   Sorting is computed on-device in this assignment. For a larger production app, this would be computed on the server side to save client CPU.

---

## What I'd Improve with More Time

1. **Persistence**: Implement a local database (such as Room) to cache the leaderboard state for offline viewing.
2. **Incremental Updates**: Use a self-balancing binary search tree (like a Red-Black tree) or skip list to update rankings in `O(log N)` rather than sorting the full list on every event.
3. **Batching**: Debounce or batch incoming score events so the leaderboard recomputes at fixed intervals (e.g. once per second) rather than on every individual event, saving battery.
4. **WebSocket/gRPC Integration**: Connect to a live backend rather than relying on local simulation.

---

## Technical Documentation

* [Architecture Design & Technical Decisions](ARCHITECTURE.md)
* [Live Tournament Anti-Cheat Strategy](ANTI_CHEAT.md)

