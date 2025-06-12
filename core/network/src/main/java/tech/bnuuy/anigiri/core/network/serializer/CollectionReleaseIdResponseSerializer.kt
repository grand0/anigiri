package tech.bnuuy.anigiri.core.network.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeCollection
import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType
import tech.bnuuy.anigiri.core.network.datasource.response.CollectionReleaseIdResponse

@OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
object CollectionReleaseIdResponseSerializer : KSerializer<CollectionReleaseIdResponse> {
    override val descriptor: SerialDescriptor = buildSerialDescriptor(
        "CollectionReleaseIdResponse",
        StructureKind.LIST,
    )

    override fun serialize(
        encoder: Encoder,
        value: CollectionReleaseIdResponse
    ) {
        encoder.encodeCollection(descriptor, 2) {
            encodeIntElement(descriptor, 0, value.id)
            encodeStringElement(descriptor, 1, value.collectionType.value)
            endStructure(descriptor)
        }
    }

    override fun deserialize(decoder: Decoder): CollectionReleaseIdResponse {
        val dec = decoder.beginStructure(descriptor)
        var id: Int? = null
        var type: String? = null

        while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> id = dec.decodeIntElement(descriptor, 0)
                1 -> type = dec.decodeStringElement(descriptor, 1)
                CompositeDecoder.DECODE_DONE -> break
                else -> throw SerializationException("Unexpected index: $index")
            }
        }
        dec.endStructure(descriptor)

        return CollectionReleaseIdResponse(
            id = id ?: throw SerializationException("Missing id"),
            collectionType = CollectionType.byValue(
                type ?: throw SerializationException("Missing collectionType")
            ) ?: throw SerializationException("Unknown collectionType")
        )
    }
}
