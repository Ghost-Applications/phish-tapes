@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package nes.app.playback

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import androidx.annotation.OptIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.media3.cast.CastPlayer
import androidx.media3.cast.SessionAvailabilityListener
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.MediaItemsWithStartPosition
import androidx.media3.session.SessionError
import arrow.core.getOrElse
import arrow.core.toOption
import com.google.android.gms.cast.framework.CastContext
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.guava.future
import nes.app.MainActivity
import nes.app.util.MediaItemsWrapper
import timber.log.Timber
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class PlaybackService : MediaLibraryService(), SessionAvailabilityListener,
    MediaLibraryService.MediaLibrarySession.Callback {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var mediaSession: MediaLibrarySession? = null
    private var exoPlayer: Player? = null
    private var castPlayer: Player? = null

    @Inject lateinit var mediaItemTree: MediaItemTree
    @Inject lateinit var replaceableForwardingPlayer: ReplaceableForwardingPlayerFactory

    lateinit var player: ReplaceableForwardingPlayer

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        val exoPlayer = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                true
            )
            .setHandleAudioBecomingNoisy(true)
            .setSkipSilenceEnabled(true)
            .build()

        val castContext = CastContext.getSharedInstance(this, MoreExecutors.directExecutor())
            .addOnFailureListener {
                Timber.e(it, "Error getting the cast session")
            }
            .result

        castPlayer = CastPlayer(castContext).apply {
            setSessionAvailabilityListener(this@PlaybackService)
        }

        val player = replaceableForwardingPlayer.create(exoPlayer)
        val pendingIntent = PendingIntent.getActivity(
            this,
            1337,
            Intent(this, MainActivity::class.java),
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        )

        mediaSession = MediaLibrarySession.Builder(this, player, this)
            .setSessionActivity(pendingIntent)
            .build()
        this.exoPlayer = exoPlayer
        this.player = player
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
            serviceScope.cancel()
        }
        super.onDestroy()
    }

    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaLibrarySession? = mediaSession

    // User dismissed the app from recent tasks
    override fun onTaskRemoved(rootIntent: Intent?) {
        mediaSession?.player?.run {
            if (playWhenReady || mediaItemCount == 0 || playbackState == Player.STATE_ENDED) {
                // stop player if it's not playing otherwise allow it to continue playing
                // in the background
                stopSelf()
            }
        }
    }

    override fun onCastSessionAvailable() {
        castPlayer?.let {
            player?.setPlayer(it)
        }
    }

    override fun onCastSessionUnavailable() {
        exoPlayer?.let {
            player?.setPlayer(it)
        }
    }


    override fun onPlaybackResumption(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo
    ): ListenableFuture<MediaItemsWithStartPosition> {
        return serviceScope.future(Dispatchers.Main) {
            MediaItemsWithStartPosition(
                player.playlist,
                player.currentPlaylistIndex,
                player.currentPosition
            )
        }
    }

    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        val sessionCommands = MediaSession.ConnectionResult.DEFAULT_SESSION_AND_LIBRARY_COMMANDS
        if (
            session.isMediaNotificationController(controller) ||
            session.isAutomotiveController(controller) ||
            session.isAutoCompanionController(controller)
        ) {
            // Available session commands to accept incoming custom commands from Auto.
            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(sessionCommands)
                .build()
        }
        // Default commands with default custom layout for all other controllers.
        return MediaSession.ConnectionResult.AcceptedResultBuilder(session).build()
    }

    override fun onGetLibraryRoot(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        return serviceScope.future {
            val item = mediaItemTree.getRoot()
            LibraryResult.ofItem(item, params)
        }
    }

    override fun onGetChildren(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        Timber.d("onGetChildren() parentId=%s", parentId)
        return serviceScope.future {
            val items = mediaItemTree.getChildren(parentId)
            LibraryResult.ofItemList(items, params)
        }
    }

    override fun onGetItem(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        mediaId: String
    ): ListenableFuture<LibraryResult<MediaItem>> {
        Timber.d("onGetItem() mediaId=%s", mediaId)
        return serviceScope.future {
            mediaItemTree.getItem(mediaId).toOption()
                .map { LibraryResult.ofItem(it, null) }
                .getOrElse { LibraryResult.ofError(SessionError.ERROR_INVALID_STATE) }
        }
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: List<MediaItem>
    ): ListenableFuture<List<MediaItem>> {
        Timber.d("onAddMediaItems() mediaItems=%s", MediaItemsWrapper(mediaItems))
        return serviceScope.future {
            if (mediaItems.size == 1 && mediaItems.first().localConfiguration == null) {
                mediaItemTree.getChildren(mediaItems.first().mediaId).let { playlist ->
                    return@future playlist
                }
            }

            mediaItems.map {
                if (it.localConfiguration == null) {
                    mediaItemTree.getItem(it.mediaId) ?: it
                } else {
                    it
                }
            }
        }
    }
}
