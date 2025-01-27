package tech.bnuuy.anigiri.feature.release.data.mapper

import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.http.parseUrl
import kotlinx.datetime.Instant
import tech.bnuuy.anigiri.core.network.datasource.response.EpisodeResponse
import tech.bnuuy.anigiri.feature.release.BuildConfig
import tech.bnuuy.anigiri.feature.release.api.data.model.Episode
import kotlin.time.DurationUnit
import kotlin.time.toDuration

internal fun List<EpisodeResponse>.mapList(): List<Episode> = map {
    it.toDomain()
}

internal fun EpisodeResponse.toDomain(): Episode {
    val srcUrl = preview.srcUrl?.let {
        URLBuilder(parseUrl(BuildConfig.ANILIBRIA_STORAGE_BASE_URL)!!)
            .appendPathSegments(it)
            .toString()
    }
    val thumbUrl = preview.thumbnailUrl?.let {
        URLBuilder(parseUrl(BuildConfig.ANILIBRIA_STORAGE_BASE_URL)!!)
            .appendPathSegments(it)
            .toString()
    }

    return Episode(
        id = id,
        name = name,
        ordinal = sortOrder,
        opening = Episode.StartEndTimestamps(
            start = opening.start?.toDuration(DurationUnit.SECONDS),
            end = opening.end?.toDuration(DurationUnit.SECONDS),
        ),
        ending = Episode.StartEndTimestamps(
            start = ending.start?.toDuration(DurationUnit.SECONDS),
            end = ending.end?.toDuration(DurationUnit.SECONDS),
        ),
        previewUrl = srcUrl,
        thumbnailUrl = thumbUrl,
        mediaStreams = Episode.MediaStreams(
            hls480 = hls480,
            hls720 = hls720,
            hls1080 = hls1080,
        ),
        duration = duration.toDuration(DurationUnit.SECONDS),
        updatedAt = Instant.parse(updatedAt),
        nameEng = nameEng,
    )
}
