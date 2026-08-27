package net.budsapps.matchit2.model

/**
 * Pure game-rules engine, ported from the original [net.budsapps.matchit.GameEngine].
 *
 * Unlike the 2012 version, this holds no mutable state itself: every function takes the
 * current [GameState] and returns a new one, which keeps it trivial to unit test and to
 * drive from a Compose [androidx.lifecycle.ViewModel].
 */
object GameEngine {

    /** Starts a fresh, unshuffled-looking but randomized board for [difficulty]. */
    fun newGame(difficulty: Difficulty): GameState {
        val values = (1..difficulty.pairCount).flatMap { listOf(it, it) }.shuffled()
        val cards = values.mapIndexed { index, value -> Card(position = index, value = value) }
        return GameState(difficulty = difficulty, cards = cards)
    }

    /**
     * Handles a click on the card at [position]. Returns the state unchanged if the click
     * can't be processed (already matched, or already the sole selected card).
     */
    fun selectCard(state: GameState, position: Int): GameState {
        val clicked = state.cards.getOrNull(position) ?: return state
        if (clicked.isMatched) return state
        if (state.selectedPosition == position) return state

        val previousPosition = state.selectedPosition
        if (previousPosition == null) {
            // First card of a pair: just reveal it.
            return state.copy(
                selectedPosition = position,
                clicks = state.clicks + 1,
                hasStarted = true
            )
        }

        // Second card of a pair: compare against the previously selected one.
        val previous = state.cards[previousPosition]
        val isMatch = previous.value == clicked.value

        val updatedCards = if (isMatch) {
            state.cards.map { card ->
                if (card.position == previousPosition || card.position == position) {
                    card.copy(isMatched = true)
                } else {
                    card
                }
            }
        } else {
            state.cards
        }

        val won = isMatch && updatedCards.all { it.isMatched }

        return state.copy(
            cards = updatedCards,
            // On a match, deselect. On a mismatch, the new card becomes the "selected" one,
            // matching the original app's behavior of leaving the latest click face-up.
            selectedPosition = if (isMatch) null else position,
            clicks = state.clicks + 1,
            isWon = won
        )
    }
}
