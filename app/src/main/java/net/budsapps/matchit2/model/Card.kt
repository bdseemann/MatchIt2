package net.budsapps.matchit2.model

import kotlinx.serialization.Serializable

/**
 * A single board position. [value] identifies which pair a card belongs to (1-based,
 * matching the card_<value>.png drawable naming), independent of on-screen [position].
 */
@Serializable
data class Card(
    val position: Int,
    val value: Int,
    val isMatched: Boolean = false
)
