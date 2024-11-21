package nes.app.ui.year

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import nes.app.data.Title
import nes.app.ui.components.CastButton
import nes.app.ui.components.SelectionData
import nes.app.ui.components.SelectionScreen
import nes.app.ui.player.PlayerState
import nes.app.ui.player.PlayerViewModel
import nes.app.util.LCE
import nes.app.util.mapCollection
import nes.networking.phishin.model.YearData

@OptIn(UnstableApi::class)
@Composable
fun YearSelectionScreen(
    viewModel: YearSelectionViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel = hiltViewModel(),
    onYearClicked: (year: String) -> Unit,
    onMiniPlayerClick: (title: Title) -> Unit,
    navigateToAboutScreen: () -> Unit,
) {
    val state: LCE<List<YearData>, Throwable> by viewModel.years.collectAsState()
    val playerState by playerViewModel.playerState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    YearSelectionScreen(
        yearData = state,
        onYearClicked = onYearClicked,
        onMiniPlayerClick = onMiniPlayerClick,
        playerState = playerState,
        onPauseAction = playerViewModel::pause,
        onPlayAction = playerViewModel::play,
        actions = {
            CastButton()
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options"
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    leadingIcon = { Icon(Icons.Default.Info, null) },
                    onClick = {
                        showMenu = false
                        navigateToAboutScreen()
                    },
                    text = { Text("About") }
                )
            }
        }
    )
}

@Composable
fun YearSelectionScreen(
    yearData: LCE<List<YearData>, Throwable>,
    onYearClicked: (year: String) -> Unit,
    onMiniPlayerClick: (title: Title) -> Unit,
    playerState: PlayerState,
    onPauseAction: () -> Unit,
    onPlayAction: () -> Unit,
    actions: @Composable RowScope.() -> Unit,
) {
    val selectionData = yearData.mapCollection {
        SelectionData(title = it.date, subtitle = "${it.show_count} shows") {
            onYearClicked(it.date)
        }
    }

    SelectionScreen(
        state = selectionData,
        upClick = null,
        onMiniPlayerClick = onMiniPlayerClick,
        playerState = playerState,
        onPauseAction = onPauseAction,
        onPlayAction = onPlayAction,
        actions = actions
    )
}
