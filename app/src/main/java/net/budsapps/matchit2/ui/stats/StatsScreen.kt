package net.budsapps.matchit2.ui.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.budsapps.matchit2.R
import net.budsapps.matchit2.model.Difficulty
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(viewModel: StatsViewModel, onNavigateBack: () -> Unit) {
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsState()
    val stats by viewModel.selectedStats.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val decimalFormat = remember { DecimalFormat("#0.00") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.stats_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                    readOnly = true,
                    value = stringResource(difficultyLabelRes(selectedDifficulty)),
                    onValueChange = {},
                    label = { Text(stringResource(R.string.difficulty_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Difficulty.entries.forEach { difficulty ->
                        DropdownMenuItem(
                            text = { Text(stringResource(difficultyLabelRes(difficulty))) },
                            onClick = {
                                viewModel.selectDifficulty(difficulty)
                                expanded = false
                            }
                        )
                    }
                }
            }

            StatRow(stringResource(R.string.games_played_label), stats.totalGames.toString())
            StatRow(stringResource(R.string.average_clicks_label), decimalFormat.format(stats.averageClicks))
            StatRow(stringResource(R.string.min_clicks_label), stats.minClicks.toString())
            StatRow(stringResource(R.string.max_clicks_label), stats.maxClicks.toString())
            StatRow(stringResource(R.string.games_quit_label), stats.gamesQuit.toString())

            Row(
                modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { viewModel.resetSelectedLevel() }) {
                    Text(stringResource(R.string.reset_level_stats))
                }
                Button(onClick = { viewModel.resetAll() }) {
                    Text(stringResource(R.string.reset_all_stats))
                }
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

fun difficultyLabelRes(difficulty: Difficulty): Int = when (difficulty) {
    Difficulty.VERY_EASY -> R.string.difficulty_very_easy
    Difficulty.EASY -> R.string.difficulty_easy
    Difficulty.MEDIUM -> R.string.difficulty_medium
    Difficulty.HARD -> R.string.difficulty_hard
    Difficulty.VERY_HARD -> R.string.difficulty_very_hard
}
