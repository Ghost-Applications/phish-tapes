package nes.app.ui.about

import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import io.noties.markwon.Markwon
import nes.app.ui.components.navigationUpIcon
import nes.app.ui.theme.NesTheme

@Composable
fun AboutScreen(
    viewModel: AboutViewModel = hiltViewModel(),
    navigateUpClick: () -> Unit
) {
    val aboutText by viewModel.aboutText.collectAsState()
    AboutScreen(aboutText, navigateUpClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    aboutText: AboutText,
    navigateUpClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("About") },
                navigationIcon = navigationUpIcon(navigateUpClick)
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AndroidView(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize(),
                factory = { context ->
                    val markwon = Markwon.create(context)
                    TextView(context).also { tv ->
                        markwon.setMarkdown(
                            tv,
                            aboutText.value
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    NesTheme {
        AboutScreen { }
    }
}
