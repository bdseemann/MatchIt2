package net.budsapps.matchit2.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.budsapps.matchit2.model.GameState

/**
 * Persists the in-progress board so the game survives process death / app restart, the same
 * behavior as the original app's onStop/onStart save-to-file, without depending on Java
 * Serializable class layout staying stable across app versions.
 */
class GameStateRepository(private val context: Context) {

    private val gameStateKey = stringPreferencesKey("in_progress_game")
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun loadGameState(): GameState? {
        val raw = context.matchItDataStore.data.first()[gameStateKey]
        return raw?.let { runCatching { json.decodeFromString<GameState>(it) }.getOrNull() }
    }

    suspend fun saveGameState(state: GameState) {
        context.matchItDataStore.edit { prefs ->
            prefs[gameStateKey] = json.encodeToString(state)
        }
    }

    suspend fun clearGameState() {
        context.matchItDataStore.edit { prefs ->
            prefs.remove(gameStateKey)
        }
    }
}
