package net.budsapps.matchit2.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.budsapps.matchit2.MatchItApplication
import net.budsapps.matchit2.ui.game.GameScreen
import net.budsapps.matchit2.ui.game.GameViewModel
import net.budsapps.matchit2.ui.settings.SettingsScreen
import net.budsapps.matchit2.ui.settings.SettingsViewModel
import net.budsapps.matchit2.ui.stats.StatsScreen
import net.budsapps.matchit2.ui.stats.StatsViewModel

private object Routes {
    const val GAME = "game"
    const val STATS = "stats"
    const val SETTINGS = "settings"
}

@Composable
fun MatchItNavHost(application: MatchItApplication, navController: NavHostController = rememberNavController()) {
    val factory = MatchItViewModelFactory(application)

    NavHost(navController = navController, startDestination = Routes.GAME) {
        composable(Routes.GAME) {
            val viewModel: GameViewModel = viewModel(factory = factory)
            GameScreen(
                viewModel = viewModel,
                onNavigateToStats = { navController.navigate(Routes.STATS) },
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }
        composable(Routes.STATS) {
            val viewModel: StatsViewModel = viewModel(factory = factory)
            StatsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.SETTINGS) {
            val viewModel: SettingsViewModel = viewModel(factory = factory)
            SettingsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
        }
    }
}
