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

/** Row/column count for a board that fits the screen without scrolling, in portrait orientation. */
fun gridSizeFor(difficulty: Difficulty): GridSize = when (difficulty) {
    Difficulty.VERY_EASY -> GridSize(rows = 3, columns = 2)
    Difficulty.EASY -> GridSize(rows = 4, columns = 2)
    Difficulty.MEDIUM -> GridSize(rows = 5, columns = 2)
    Difficulty.HARD -> GridSize(rows = 4, columns = 3)
    Difficulty.VERY_HARD -> GridSize(rows = 4, columns = 4)
}

data class GridSize(val rows: Int, val columns: Int)
