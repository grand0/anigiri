package tech.bnuuy.anigiri.core.network.datasource.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseId(
    @SerialName("release_id")
    val releaseId: Int,
)
