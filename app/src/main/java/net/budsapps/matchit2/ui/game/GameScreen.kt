package net.budsapps.matchit2.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.budsapps.matchit2.R
import net.budsapps.matchit2.model.Card
import net.budsapps.matchit2.model.GameState
import net.budsapps.matchit2.ui.theme.MatchedCardBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onNavigateToStats: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var menuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(state.isWon) {
        if (state.isWon) {
            snackbarHostState.showSnackbar(
                message = "It took ${state.clicks} clicks!"
            )
            viewModel.startNewGame()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = null)
                    }
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.end_game)) },
                            enabled = state.hasStarted,
                            onClick = {
                                menuExpanded = false
                                viewModel.endGame()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.view_stats)) },
                            enabled = !state.hasStarted,
                            onClick = {
                                menuExpanded = false
                                onNavigateToStats()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.settings)) },
                            enabled = !state.hasStarted,
                            onClick = {
                                menuExpanded = false
                                onNavigateToSettings()
                            }
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(stringResource(R.string.clicks_label), style = MaterialTheme.typography.bodyLarge)
                Text(state.clicks.toString(), style = MaterialTheme.typography.bodyLarge)
            }

            GameBoard(state = state, onCardClick = viewModel::onCardClicked)
        }
    }
}

/** Width:height ratio of the card artwork, used to size tiles without distorting them. */
private const val CardAspectRatio = 0.75f
private val BoardSpacing = 8.dp

@Composable
private fun GameBoard(state: GameState, onCardClick: (Int) -> Unit) {
    val gridSize = gridSizeFor(state.difficulty)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Fit tiles to whichever dimension is tighter so the whole board is always
        // visible without scrolling, rather than letting width alone drive height.
        val maxCellWidth = (maxWidth - BoardSpacing * (gridSize.columns - 1)) / gridSize.columns
        val maxCellHeight = (maxHeight - BoardSpacing * (gridSize.rows - 1)) / gridSize.rows
        val cellWidth: Dp
        val cellHeight: Dp
        if (maxCellWidth / CardAspectRatio <= maxCellHeight) {
            cellWidth = maxCellWidth
            cellHeight = maxCellWidth / CardAspectRatio
        } else {
            cellHeight = maxCellHeight
            cellWidth = maxCellHeight * CardAspectRatio
        }

        Column(verticalArrangement = Arrangement.spacedBy(BoardSpacing)) {
            state.cards.chunked(gridSize.columns).forEach { rowCards ->
                Row(horizontalArrangement = Arrangement.spacedBy(BoardSpacing)) {
                    rowCards.forEach { card ->
                        val isFaceUp = card.isMatched || card.position == state.selectedPosition
                        CardTile(
                            card = card,
                            isFaceUp = isFaceUp,
                            onClick = { onCardClick(card.position) },
                            modifier = Modifier.size(cellWidth, cellHeight)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardTile(card: Card, isFaceUp: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val borderColor = if (card.isMatched) MatchedCardBorder else MaterialTheme.colorScheme.outline
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(enabled = !card.isMatched, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(
                if (isFaceUp) cardDrawableRes(card.value) else R.drawable.card_back
            ),
            contentDescription = if (isFaceUp) {
                stringResource(R.string.card_front_description)
            } else {
                stringResource(R.string.card_back_description)
            },
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}
