package nes.app.ui.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.Paparazzi
import nes.app.noShowPlayerState
import nes.app.showingPlayerState
import nes.app.ui.PaparazziNightTest
import nes.app.ui.PaparazziTest
import org.junit.Test

class MiniPlayerTest : PaparazziTest() {

    @Test
    fun `should not show mini player when not playing`() {
        paparazzi.snapshot(noShowPlayerState)
    }

    @Test
    fun `show mini player paused`() {
        paparazzi.snapshot(showingPlayerState.copy(isPlaying = false))
    }

    @Test
    fun `show mini player playing`() {
        paparazzi.snapshot(showingPlayerState)
    }
}

class MiniPlayerNightTest : PaparazziNightTest() {

    @Test
    fun `should not show mini player when not playing`() {
        paparazzi.snapshot(noShowPlayerState)
    }

    @Test
    fun `show mini player paused`() {
        paparazzi.snapshot(showingPlayerState.copy(isPlaying = false))
    }

    @Test
    fun `show mini player playing`() {
        paparazzi.snapshot(showingPlayerState)
    }
}

private fun Paparazzi.snapshot(state: PlayerState) {
    snapshot {
        Box(
            modifier = Modifier.fillMaxSize()
                .height(64.dp)
        ) {
            MiniPlayer(
                playerState = state,
                onClick = {},
                onPlayAction = {},
                onPauseAction = {}
            )
        }
    }
}
