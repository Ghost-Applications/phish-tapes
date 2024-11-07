package nes.app.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import nes.app.data.Title

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopAppBarText(title: Title) {
    Text(
        text = title.value,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.basicMarquee(
            iterations = Int.MAX_VALUE
        )
    )
}
