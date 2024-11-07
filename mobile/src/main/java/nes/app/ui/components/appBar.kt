package nes.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nes.app.R

fun navigationUpIcon(upClick: () -> Unit): @Composable () -> Unit = {
    IconButton(onClick = upClick) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.navigate_back)
        )
    }
}

fun phishTapesIcon(): @Composable () -> Unit = {
    Image(
        modifier = Modifier.size(48.dp),
        painter = painterResource(id = R.drawable.app_icon),
        contentDescription = null
    )
}
