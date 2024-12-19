package nes.networking.phishnet.model

import kotlinx.serialization.Serializable
import nes.networking.serializers.LongToStringSerializer

@Serializable
data class SetListResponse(
    val showid: Long,
    val showdate: String,
    @Serializable(with = LongToStringSerializer::class)
    val venueid: String,
    val venue: String,
    val city: String,
    val setlistnotes: String,
    val song: String,
)
