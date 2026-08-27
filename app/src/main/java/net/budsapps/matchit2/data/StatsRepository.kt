package net.budsapps.matchit2.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.budsapps.matchit2.model.Difficulty
import net.budsapps.matchit2.model.StatsSnapshot

/** Ported from the original StatsData class; persisted as JSON instead of Java serialization. */
class StatsRepository(private val context: Context) {

    private val statsKey = stringPreferencesKey("stats_snapshot")
    private val json = Json { ignoreUnknownKeys = true }

    val statsFlow: Flow<StatsSnapshot> = context.matchItDataStore.data.map { prefs ->
        prefs[statsKey]?.let { raw ->
            runCatching { json.decodeFromString<StatsSnapshot>(raw) }.getOrNull()
        } ?: StatsSnapshot()
    }

    suspend fun recordCompletedGame(difficulty: Difficulty, clicks: Int) =
        update { it.withCompletedGame(difficulty, clicks) }

    suspend fun recordQuit(difficulty: Difficulty) =
        update { it.withQuit(difficulty) }

    suspend fun resetStats(difficulty: Difficulty) =
        update { it.withReset(difficulty) }

    suspend fun resetAllStats() = update { StatsSnapshot() }

    private suspend fun update(transform: (StatsSnapshot) -> StatsSnapshot) {
        context.matchItDataStore.edit { prefs ->
            val current = prefs[statsKey]?.let { raw ->
                runCatching { json.decodeFromString<StatsSnapshot>(raw) }.getOrNull()
            } ?: StatsSnapshot()
            prefs[statsKey] = json.encodeToString(transform(current))
        }
    }
}
