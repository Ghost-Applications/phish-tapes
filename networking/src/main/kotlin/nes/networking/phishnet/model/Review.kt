package nes.networking.phishnet.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Review(
    val reviewid: Int,
    val uid: Int,
    val username: String,
    val showid: Int,
    val score: Int,
    val venue: String,
    val city: String,
    val state: String,
    val country: String,
    @Json(name = "review_text")
    val reviewText: String,
    @Json(name = "posted_at")
    val postedAt: String
)
