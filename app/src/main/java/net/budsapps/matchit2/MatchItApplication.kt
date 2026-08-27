package net.budsapps.matchit2

import android.app.Application
import net.budsapps.matchit2.data.GameStateRepository
import net.budsapps.matchit2.data.SettingsRepository
import net.budsapps.matchit2.data.StatsRepository

class MatchItApplication : Application() {
    val settingsRepository by lazy { SettingsRepository(this) }
    val statsRepository by lazy { StatsRepository(this) }
    val gameStateRepository by lazy { GameStateRepository(this) }
}
