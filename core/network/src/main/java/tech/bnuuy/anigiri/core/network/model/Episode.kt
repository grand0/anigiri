package tech.bnuuy.anigiri.core.network.model

import kotlinx.datetime.Instant
import kotlin.time.Duration

data class Episode(
    val id: String,
    val name: String?,
    val nameEng: String?,
    val ordinal: Int,
    val opening: StartEndTimestamps,
    val ending: StartEndTimestamps,
    val previewUrl: String?,
    val thumbnailUrl: String?,
    val mediaStreams: MediaStreams,
    val duration: Duration,
    val updatedAt: Instant,
) {
    
    data class StartEndTimestamps(
        val start: Duration?,
        val end: Duration?,
    )
    
    data class MediaStreams(
        val hls480: String?,
        val hls720: String?,
        val hls1080: String?,
    )
}
