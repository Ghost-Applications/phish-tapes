package nes.app.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import nes.app.data.Title
import nes.app.ui.about.AboutScreen
import nes.app.ui.player.FullPlayer
import nes.app.ui.show.ShowScreen
import nes.app.ui.show.ShowSelectionScreen
import nes.app.ui.year.YearSelectionScreen

@UnstableApi
@ExperimentalMaterial3Api
@Composable
fun NesNavController(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.YearSelection.route) {
        val miniPlayerClicked = { title: Title ->
            navController.navigate(Screen.Player.createRoute(title))
        }

        composable(route = Screen.YearSelection.route) {
            YearSelectionScreen(
                onMiniPlayerClick = miniPlayerClicked,
                onYearClicked = { navController.navigate(Screen.ShowSelection.createRoute(it)) },
                navigateToAboutScreen = { navController.navigate(Screen.About.route) }
            )
        }
        composable(
            route = Screen.ShowSelection.route,
            arguments = Screen.ShowSelection.navArguments
        ) {
            ShowSelectionScreen(
                navigateUpClick = { navController.navigateUp() },
                onShowClicked = { id, venue -> navController.navigate(
                    Screen.Show.createRoute(
                        id,
                        venue
                    )
                ) },
                onMiniPlayerClick = miniPlayerClicked
            )
        }
        composable(
            route = Screen.Show.route,
            arguments = Screen.Show.navArguments
        ) {
            ShowScreen(
                upClick = { navController.navigateUp() },
                onMiniPlayerClick = miniPlayerClicked
            )
        }
        composable(
            route = Screen.Player.route,
            arguments = Screen.Player.navArguments
        ) {
            FullPlayer(
                navigateToShow = { id, name ->
                    navController.clearBackStack(Screen.Player.route)
                    navController.navigate(Screen.Show.createRoute(id, name)) {
                        popUpTo(Screen.YearSelection.route)
                    }
                },
                upClick = { navController.navigateUp() },
            )
        }

        composable(Screen.About.route) {
            AboutScreen {
                navController.navigateUp()
            }
        }
    }
}
