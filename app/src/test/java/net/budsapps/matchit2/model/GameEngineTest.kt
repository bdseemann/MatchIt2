package net.budsapps.matchit2.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameEngineTest {

    @Test
    fun `newGame creates the right number of cards for each difficulty`() {
        Difficulty.entries.forEach { difficulty ->
            val state = GameEngine.newGame(difficulty)
            assertEquals(difficulty.cardCount, state.cards.size)
        }
    }

    @Test
    fun `newGame creates exactly two cards per pair value`() {
        val state = GameEngine.newGame(Difficulty.HARD)
        val counts = state.cards.groupingBy { it.value }.eachCount()
        assertEquals(Difficulty.HARD.pairCount, counts.size)
        assertTrue(counts.values.all { it == 2 })
    }

    @Test
    fun `newGame starts with no card selected, no clicks, not started, not won`() {
        val state = GameEngine.newGame(Difficulty.EASY)
        assertNull(state.selectedPosition)
        assertEquals(0, state.clicks)
        assertFalse(state.hasStarted)
        assertFalse(state.isWon)
    }

    @Test
    fun `selecting the first card of a pair reveals it and counts a click`() {
        val state = twoCardState(matchingValues = true)

        val result = GameEngine.selectCard(state, 0)

        assertEquals(0, result.selectedPosition)
        assertEquals(1, result.clicks)
        assertTrue(result.hasStarted)
        assertFalse(result.cards[0].isMatched)
    }

    @Test
    fun `re-clicking the already selected card is a no-op`() {
        val state = GameEngine.selectCard(twoCardState(matchingValues = true), 0)

        val result = GameEngine.selectCard(state, 0)

        assertEquals(state, result)
    }

    @Test
    fun `clicking a matching second card marks both matched and deselects`() {
        val state = GameEngine.selectCard(twoCardState(matchingValues = true), 0)

        val result = GameEngine.selectCard(state, 1)

        assertTrue(result.cards[0].isMatched)
        assertTrue(result.cards[1].isMatched)
        assertNull(result.selectedPosition)
        assertEquals(2, result.clicks)
    }

    @Test
    fun `matching the last pair wins the game`() {
        val state = GameEngine.selectCard(twoCardState(matchingValues = true), 0)

        val result = GameEngine.selectCard(state, 1)

        assertTrue(result.isWon)
    }

    @Test
    fun `clicking a mismatching second card leaves cards unmatched and selects the new card`() {
        val state = GameEngine.selectCard(twoCardState(matchingValues = false), 0)

        val result = GameEngine.selectCard(state, 1)

        assertFalse(result.cards[0].isMatched)
        assertFalse(result.cards[1].isMatched)
        assertEquals(1, result.selectedPosition)
        assertEquals(2, result.clicks)
        assertFalse(result.isWon)
    }

    @Test
    fun `clicking an already matched card is a no-op`() {
        val matched = GameEngine.selectCard(
            GameEngine.selectCard(twoCardState(matchingValues = true), 0),
            1
        )

        val result = GameEngine.selectCard(matched, 0)

        assertEquals(matched, result)
    }

    private fun twoCardState(matchingValues: Boolean): GameState = GameState(
        difficulty = Difficulty.VERY_EASY,
        cards = listOf(
            Card(position = 0, value = 1),
            Card(position = 1, value = if (matchingValues) 1 else 2)
        )
    )
}
