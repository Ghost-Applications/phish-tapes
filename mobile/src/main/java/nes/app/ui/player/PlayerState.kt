package nes.app.ui.player

import android.net.Uri
import androidx.compose.runtime.Immutable
import nes.app.data.Title

sealed interface PlayerState {

    @Immutable
    data object NoMedia: PlayerState

    @Immutable
    data class MediaLoaded(
        val isPlaying: Boolean,
        val showId: Long,
        val venueName: String,
        val formatedElapsedTime: String,
        val formatedDurationTime: String,
        val duration: Long,
        val currentPosition: Long,
        val artworkUri: Uri?,
        val title: String,
        val albumTitle: Title,
        val mediaId: String,
    ): PlayerState
}
