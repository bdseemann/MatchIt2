package net.budsapps.matchit2.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.budsapps.matchit2.data.StatsRepository
import net.budsapps.matchit2.model.Difficulty
import net.budsapps.matchit2.model.GameStats

class StatsViewModel(private val statsRepository: StatsRepository) : ViewModel() {

    private val _selectedDifficulty = MutableStateFlow(Difficulty.DEFAULT)
    val selectedDifficulty: StateFlow<Difficulty> = _selectedDifficulty.asStateFlow()

    val selectedStats: StateFlow<GameStats> = combine(
        statsRepository.statsFlow,
        _selectedDifficulty
    ) { snapshot, difficulty -> snapshot.statsFor(difficulty) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GameStats())

    fun selectDifficulty(difficulty: Difficulty) {
        _selectedDifficulty.value = difficulty
    }

    fun resetSelectedLevel() {
        viewModelScope.launch { statsRepository.resetStats(_selectedDifficulty.value) }
    }

    fun resetAll() {
        viewModelScope.launch { statsRepository.resetAllStats() }
    }
}
