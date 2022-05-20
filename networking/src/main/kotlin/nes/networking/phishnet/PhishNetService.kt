package nes.networking.phishnet

import nes.networking.phishnet.model.PhishNetWrapper
import nes.networking.phishnet.model.Review
import nes.networking.phishnet.model.SetList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhishNetService {
    @GET("setlists/showdate/{showDate}")
    suspend fun setlist(@Path("showdate") showDate: String): PhishNetWrapper<SetList>

    @GET("reviews/query")
    suspend fun reviews(@Query("showid") showId: String): PhishNetWrapper<Review>
}
