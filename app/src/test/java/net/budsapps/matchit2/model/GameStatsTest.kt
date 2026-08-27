package net.budsapps.matchit2.model

import org.junit.Assert.assertEquals
import org.junit.Test

class GameStatsTest {

    @Test
    fun `a fresh GameStats has zeroed fields and zero average`() {
        val stats = GameStats()
        assertEquals(0, stats.totalGames)
        assertEquals(0.0, stats.averageClicks, 0.0)
    }

    @Test
    fun `first completed game sets min, max and total from that single game`() {
        val stats = GameStats().withCompletedGame(clicks = 12)

        assertEquals(1, stats.totalGames)
        assertEquals(12, stats.totalClicks)
        assertEquals(12, stats.minClicks)
        assertEquals(12, stats.maxClicks)
        assertEquals(12.0, stats.averageClicks, 0.0)
    }

    @Test
    fun `subsequent games update min and max correctly`() {
        val stats = GameStats()
            .withCompletedGame(clicks = 20)
            .withCompletedGame(clicks = 8)
            .withCompletedGame(clicks = 30)

        assertEquals(3, stats.totalGames)
        assertEquals(58, stats.totalClicks)
        assertEquals(8, stats.minClicks)
        assertEquals(30, stats.maxClicks)
        assertEquals(58.0 / 3.0, stats.averageClicks, 0.0001)
    }

    @Test
    fun `withQuit increments gamesQuit without touching click stats`() {
        val stats = GameStats().withCompletedGame(clicks = 10).withQuit()

        assertEquals(1, stats.gamesQuit)
        assertEquals(1, stats.totalGames)
    }

    @Test
    fun `StatsSnapshot keeps difficulties isolated from each other`() {
        val snapshot = StatsSnapshot()
            .withCompletedGame(Difficulty.EASY, clicks = 10)
            .withCompletedGame(Difficulty.HARD, clicks = 40)

        assertEquals(1, snapshot.statsFor(Difficulty.EASY).totalGames)
        assertEquals(10, snapshot.statsFor(Difficulty.EASY).totalClicks)
        assertEquals(1, snapshot.statsFor(Difficulty.HARD).totalGames)
        assertEquals(40, snapshot.statsFor(Difficulty.HARD).totalClicks)
        assertEquals(0, snapshot.statsFor(Difficulty.MEDIUM).totalGames)
    }

    @Test
    fun `StatsSnapshot withReset only clears the targeted difficulty`() {
        val snapshot = StatsSnapshot()
            .withCompletedGame(Difficulty.EASY, clicks = 10)
            .withCompletedGame(Difficulty.HARD, clicks = 40)
            .withReset(Difficulty.EASY)

        assertEquals(0, snapshot.statsFor(Difficulty.EASY).totalGames)
        assertEquals(1, snapshot.statsFor(Difficulty.HARD).totalGames)
    }
}
