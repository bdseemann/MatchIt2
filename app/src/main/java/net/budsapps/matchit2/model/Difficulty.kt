package net.budsapps.matchit2.model

/**
 * Board difficulty, expressed as the number of cards on the board (always an even count).
 * Mirrors the five levels from the original 2012 app.
 */
enum class Difficulty(val cardCount: Int) {
    VERY_EASY(6),
    EASY(8),
    MEDIUM(10),
    HARD(12),
    VERY_HARD(16);

    val pairCount: Int get() = cardCount / 2

    companion object {
        val DEFAULT = EASY

        fun fromCardCount(cardCount: Int): Difficulty =
            entries.firstOrNull { it.cardCount == cardCount } ?: DEFAULT
    }
}
