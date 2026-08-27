package net.budsapps.matchit2.model

import kotlinx.serialization.Serializable

/** Aggregate stats for a single [Difficulty] level, ported from the original StatsData. */
@Serializable
data class GameStats(
    val totalGames: Int = 0,
    val totalClicks: Int = 0,
    val minClicks: Int = 0,
    val maxClicks: Int = 0,
    val gamesQuit: Int = 0
) {
    val averageClicks: Double
        get() = if (totalGames > 0) totalClicks.toDouble() / totalGames else 0.0

    /** Records a finished game that took [clicks] clicks. */
    fun withCompletedGame(clicks: Int): GameStats = copy(
        totalGames = totalGames + 1,
        totalClicks = totalClicks + clicks,
        minClicks = if (minClicks <= 0 || clicks < minClicks) clicks else minClicks,
        maxClicks = if (maxClicks <= 0 || clicks > maxClicks) clicks else maxClicks
    )

    /** Records a game ended early (menu "End Game"). */
    fun withQuit(): GameStats = copy(gamesQuit = gamesQuit + 1)
}

/** Per-difficulty stats, keyed the same way the original app keyed by pair count. */
@Serializable
data class StatsSnapshot(
    val byDifficulty: Map<Difficulty, GameStats> = emptyMap()
) {
    fun statsFor(difficulty: Difficulty): GameStats = byDifficulty[difficulty] ?: GameStats()

    fun withCompletedGame(difficulty: Difficulty, clicks: Int): StatsSnapshot =
        update(difficulty) { it.withCompletedGame(clicks) }

    fun withQuit(difficulty: Difficulty): StatsSnapshot =
        update(difficulty) { it.withQuit() }

    fun withReset(difficulty: Difficulty): StatsSnapshot =
        copy(byDifficulty = byDifficulty + (difficulty to GameStats()))

    private fun update(difficulty: Difficulty, transform: (GameStats) -> GameStats): StatsSnapshot =
        copy(byDifficulty = byDifficulty + (difficulty to transform(statsFor(difficulty))))
}
