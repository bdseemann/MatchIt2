package net.budsapps.matchit2.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.budsapps.matchit2.data.GameStateRepository
import net.budsapps.matchit2.data.SettingsRepository
import net.budsapps.matchit2.data.StatsRepository
import net.budsapps.matchit2.model.Difficulty
import net.budsapps.matchit2.model.GameEngine
import net.budsapps.matchit2.model.GameState

class GameViewModel(
    private val settingsRepository: SettingsRepository,
    private val statsRepository: StatsRepository,
    private val gameStateRepository: GameStateRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameEngine.newGame(Difficulty.DEFAULT))
    val state: StateFlow<GameState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val difficulty = settingsRepository.difficultyFlow.first()
            val restored = gameStateRepository.loadGameState()
            _state.value = restored ?: GameEngine.newGame(difficulty)
        }
        // A difficulty change from Settings only affects the board once the current
        // game hasn't been started yet, mirroring the original app's behavior of only
        // reading the preference when a new game is created.
        viewModelScope.launch {
            settingsRepository.difficultyFlow.drop(1).collect { newDifficulty ->
                if (!_state.value.hasStarted && _state.value.difficulty != newDifficulty) {
                    startNewGame(newDifficulty)
                }
            }
        }
    }

    fun onCardClicked(position: Int) {
        val previous = _state.value
        val updated = GameEngine.selectCard(previous, position)
        if (updated == previous) return
        _state.value = updated
        persist(updated)

        if (updated.isWon && !previous.isWon) {
            viewModelScope.launch {
                statsRepository.recordCompletedGame(updated.difficulty, updated.clicks)
            }
        }
    }

    fun endGame() {
        val current = _state.value
        viewModelScope.launch { statsRepository.recordQuit(current.difficulty) }
        startNewGame(current.difficulty)
    }

    fun startNewGame(difficulty: Difficulty = _state.value.difficulty) {
        val fresh = GameEngine.newGame(difficulty)
        _state.value = fresh
        persist(fresh)
    }

    private fun persist(state: GameState) {
        viewModelScope.launch { gameStateRepository.saveGameState(state) }
    }
}
