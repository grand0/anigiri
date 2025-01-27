package tech.bnuuy.anigiri.core.network.datasource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeResponse(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String?,
    @SerialName("name_english")
    val nameEng: String?,
    @SerialName("sort_order")
    val sortOrder: Int,
    @SerialName("opening")
    val opening: EpisodeStartEndTimestampsResponse,
    @SerialName("ending")
    val ending: EpisodeStartEndTimestampsResponse,
    @SerialName("preview")
    val preview: EpisodePreviewResponse,
    @SerialName("hls_480")
    val hls480: String?,
    @SerialName("hls_720")
    val hls720: String?,
    @SerialName("hls_1080")
    val hls1080: String?,
    @SerialName("duration")
    val duration: Int,
    @SerialName("updated_at")
    val updatedAt: String,
) {

    @Serializable
    data class EpisodeStartEndTimestampsResponse(
        @SerialName("start")
        val start: Int?,
        @SerialName("stop")
        val end: Int?,
    )

    @Serializable
    data class EpisodePreviewResponse(
        @SerialName("src")
        val srcUrl: String?,
        @SerialName("thumbnail")
        val thumbnailUrl: String?,
    )
}
