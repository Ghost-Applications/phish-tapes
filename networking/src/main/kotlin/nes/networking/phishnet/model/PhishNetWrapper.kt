package nes.networking.phishnet.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhishNetWrapper<T>(
    val error_message: String = "",
    val data: List<T>
)
