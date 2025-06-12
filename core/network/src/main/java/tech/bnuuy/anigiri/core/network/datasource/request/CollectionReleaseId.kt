package tech.bnuuy.anigiri.core.network.datasource.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType

@Serializable
data class CollectionReleaseId(
    @SerialName("release_id")
    val id: Int,
    @SerialName("type_of_collection")
    val collectionType: CollectionType,
)
