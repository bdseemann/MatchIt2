package net.budsapps.matchit2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.budsapps.matchit2.MatchItApplication
import net.budsapps.matchit2.ui.game.GameViewModel
import net.budsapps.matchit2.ui.settings.SettingsViewModel
import net.budsapps.matchit2.ui.stats.StatsViewModel

/** Simple manual factory; the app is small enough that a DI framework isn't warranted. */
class MatchItViewModelFactory(private val app: MatchItApplication) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        GameViewModel::class.java ->
            GameViewModel(app.settingsRepository, app.statsRepository, app.gameStateRepository)
        StatsViewModel::class.java ->
            StatsViewModel(app.statsRepository)
        SettingsViewModel::class.java ->
            SettingsViewModel(app.settingsRepository)
        else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    } as T
}
