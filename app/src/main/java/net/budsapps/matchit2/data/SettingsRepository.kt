package net.budsapps.matchit2.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.budsapps.matchit2.model.Difficulty

/** Ported from the original ListPreference-backed "NO_OF_PAIRS" setting. */
class SettingsRepository(private val context: Context) {

    private val difficultyKey = stringPreferencesKey("difficulty")

    val difficultyFlow: Flow<Difficulty> = context.matchItDataStore.data.map { prefs ->
        prefs[difficultyKey]?.let { runCatching { Difficulty.valueOf(it) }.getOrNull() }
            ?: Difficulty.DEFAULT
    }

    suspend fun setDifficulty(difficulty: Difficulty) {
        context.matchItDataStore.edit { prefs ->
            prefs[difficultyKey] = difficulty.name
        }
    }
}
