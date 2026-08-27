package net.budsapps.matchit2.model

import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val difficulty: Difficulty,
    val cards: List<Card>,
    val selectedPosition: Int? = null,
    val clicks: Int = 0,
    val hasStarted: Boolean = false,
    val isWon: Boolean = false
) {
    val matchedPairCount: Int get() = cards.count { it.isMatched } / 2
}
