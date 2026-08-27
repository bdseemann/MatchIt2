package net.budsapps.matchit2.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * Single DataStore file backing settings, stats, and in-progress game state. Replaces the
 * original app's raw internal-storage files (migame.dat / mistats.dat) written with Java
 * object serialization, which broke silently whenever a field was added or removed.
 */
val Context.matchItDataStore: DataStore<Preferences> by preferencesDataStore(name = "matchit_prefs")
