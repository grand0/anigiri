package tech.bnuuy.anigiri.core.network.datasource.response

import kotlinx.serialization.Serializable
import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType
import tech.bnuuy.anigiri.core.network.serializer.CollectionReleaseIdResponseSerializer

@Serializable(with = CollectionReleaseIdResponseSerializer::class)
data class CollectionReleaseIdResponse(
    val id: Int,
    val collectionType: CollectionType,
)
