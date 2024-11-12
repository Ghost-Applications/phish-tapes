package nes.networking.phishin

import arrow.core.Either
import kotlinx.coroutines.withTimeout
import nes.networking.phishin.model.Show
import nes.networking.phishin.model.YearData
import kotlin.time.Duration.Companion.milliseconds

class PhishInRepository(
    private val phishInService: PhishInService
) {
    suspend fun years(): Either<Throwable, List<YearData>> = Either.catch {
        phishInService.years().data.reversed()
    }

    suspend fun shows(year: String): Either<Throwable, List<Show>> = Either.catch {
        phishInService.shows(year).data
    }

    suspend fun show(showId: String): Either<Throwable, Show> = Either.catch {
        phishInService.show(showId).data
    }
}
