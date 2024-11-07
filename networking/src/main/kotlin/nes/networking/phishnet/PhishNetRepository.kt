package nes.networking.phishnet

import arrow.core.Either
import kotlinx.coroutines.withTimeout
import nes.networking.phishnet.model.Review
import nes.networking.phishnet.model.SetList
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class PhishNetRepository @Inject constructor(
    private val phishNetService: PhishNetService
) {
    suspend fun setlist(showDate: String): Either<Throwable, SetList> = Either.catch {
        val result = phishNetService.setlist(showDate)
        val data = result.data
        if (data.isEmpty()) {
            error(result.toString())
        }
        val firstSong = data[0]
        val songs = data.map { it.song }
        val setListNotes = data.map { it.setlistnotes }

        SetList(
            showid = firstSong.showid,
            showdate = firstSong.showdate,
            venue = firstSong.venue,
            city = firstSong.city,
            setlistnotes = setListNotes.joinToString(),
            songs = songs
        )
    }

    suspend fun reviews(showId: String): Either<Throwable, List<Review>> = Either.catch {
        phishNetService.reviews(showId).data
    }
}
