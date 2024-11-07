package nes.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nes.app.R
import nes.app.data.Title
import nes.app.util.LCE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> NesScaffold(
    title: Title,
    state: LCE<T, Any>,
    upClick: (() -> Unit)?,
    actions: @Composable RowScope.() -> Unit,
    content: @Composable (value: T) -> Unit
) {
    val titleComposable: @Composable () -> Unit = { TopAppBarText(title) }

    val appBar: @Composable () -> Unit = {
        if (upClick == null) {
            CenterAlignedTopAppBar(
                title = titleComposable,
                navigationIcon = phishTapesIcon(),
                actions = actions
            )
        } else {
            TopAppBar(
                title = titleComposable,
                navigationIcon = navigationUpIcon(upClick),
                actions = actions
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = appBar
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when(state) {
                is LCE.Error -> ErrorScreen(state.userDisplayedMessage)
                is LCE.Content -> content(state.value)
                LCE.Loading -> LoadingScreen()
            }
        }
    }
}
