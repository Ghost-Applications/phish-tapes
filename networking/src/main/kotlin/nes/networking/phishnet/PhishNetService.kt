package nes.networking.phishnet

import nes.networking.phishnet.model.PhishNetWrapper
import nes.networking.phishnet.model.Review
import nes.networking.phishnet.model.SetList
import nes.networking.phishnet.model.SetListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhishNetService {
    @GET("setlists/showdate/{showdate}")
    suspend fun setlist(@Path("showdate") showDate: String): PhishNetWrapper<SetListResponse>

    @GET("reviews/showid/{showid}")
    suspend fun reviews(@Path("showid") showId: String): PhishNetWrapper<Review>
}
