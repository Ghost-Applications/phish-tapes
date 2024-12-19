package nes.networking.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object LongToStringSerializer: KSerializer<String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("nes.longToString", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): String {
        return decoder.decodeLong().toString()
    }

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeLong(value.toLong())
    }
}
