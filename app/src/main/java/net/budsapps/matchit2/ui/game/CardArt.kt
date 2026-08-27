package net.budsapps.matchit2.ui.game

import androidx.annotation.DrawableRes
import net.budsapps.matchit2.R
import net.budsapps.matchit2.model.Difficulty

@DrawableRes
fun cardDrawableRes(value: Int): Int = when (value) {
    1 -> R.drawable.card_1
    2 -> R.drawable.card_2
    3 -> R.drawable.card_3
    4 -> R.drawable.card_4
    5 -> R.drawable.card_5
    6 -> R.drawable.card_6
    7 -> R.drawable.card_7
    else -> R.drawable.card_8
}

/** Column count for a reasonably square board per difficulty, in portrait orientation. */
fun columnsFor(difficulty: Difficulty): Int = when (difficulty) {
    Difficulty.VERY_EASY -> 2
    Difficulty.EASY -> 2
    Difficulty.MEDIUM -> 2
    Difficulty.HARD -> 3
    Difficulty.VERY_HARD -> 4
}
