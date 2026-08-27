package net.budsapps.matchit2.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.budsapps.matchit2.data.SettingsRepository
import net.budsapps.matchit2.model.Difficulty

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    val difficulty: StateFlow<Difficulty> = settingsRepository.difficultyFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Difficulty.DEFAULT)

    fun setDifficulty(difficulty: Difficulty) {
        viewModelScope.launch { settingsRepository.setDifficulty(difficulty) }
    }
}
