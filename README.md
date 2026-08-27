# MatchIt2

**[Download for Android](https://bdseemann.github.io/MatchIt2/)**

A modern rewrite of [MatchIt](https://github.com/bdseemann/MatchIt), a memory-matching card
game originally built in 2012 for an Android mobile development course. The original was an
Eclipse-era project targeting Android 2.2 (API 8), built with plain `Activity`/`View` code,
a hardware-menu-key options menu, and game state saved via raw Java object serialization.

This version keeps the same game and the same card artwork, rebuilt with a current Android
stack:

- **Kotlin** instead of Java
- **Jetpack Compose** (Material 3) instead of hand-built `TableLayout`/`ImageView` boards
- **ViewModel + Kotlin coroutines/`Flow`** for state instead of manual `onStart`/`onStop` wiring
- **Jetpack DataStore** (JSON via kotlinx.serialization) for settings, stats, and the
  in-progress game, replacing brittle Java `Serializable` files
- **Navigation Compose** for the Game / Stats / Settings screens, replacing separate
  `Activity` subclasses and the old options menu
- **JUnit unit tests** for the game engine and stats logic

## Gameplay

Flip two cards at a time to find matching pairs. Fewer clicks is better. Choose a difficulty
(3 to 8 pairs) in Settings, and check your best/worst/average runs per difficulty in Stats.

## Project structure

- `model/` — pure Kotlin game rules and stats logic (`GameEngine`, `GameState`, `GameStats`),
  no Android dependencies, fully unit tested under `app/src/test`.
- `data/` — DataStore-backed repositories for settings, stats, and in-progress game state.
- `ui/` — Compose screens and `ViewModel`s for the Game, Stats, and Settings screens.

## Building

Requires JDK 17+ and the Android SDK (compileSdk 35). Open in Android Studio, or from the
command line:

```
./gradlew assembleDebug
./gradlew test
```

## Credits

Card artwork carried over from the original 2012 project.
