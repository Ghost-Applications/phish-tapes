package nes.networking.phishin

import arrow.core.Either
import kotlinx.coroutines.withTimeout
import nes.networking.phishin.model.Show
import nes.networking.phishin.model.YearData
import kotlin.time.Duration.Companion.milliseconds

class PhishInRepository(
    private val phishInService: PhishInService
) {
    suspend fun years(): Either<Exception, List<YearData>> = Either.catchOrThrow {
        phishInService.years().data.reversed()
    }

    suspend fun shows(year: String): Either<Exception, List<Show>> = Either.catchOrThrow {
        phishInService.shows(year).data
    }

    suspend fun show(showId: String): Either<Exception, Show> = Either.catchOrThrow {
        phishInService.show(showId).data
    }
}
