package tech.bnuuy.anigiri.feature.home.data.mapper

import kotlinx.datetime.Instant
import tech.bnuuy.anigiri.core.network.datasource.response.ReleaseResponse
import tech.bnuuy.anigiri.core.network.util.buildStorageUrl
import tech.bnuuy.anigiri.feature.home.api.data.model.Release

internal fun List<ReleaseResponse>.mapToDomain(): List<Release> = map {
    it.toDomain()
}

internal fun ReleaseResponse.toDomain(): Release {
    val srcUrl = posters.srcUrl?.let { buildStorageUrl(it) }
    val thumbUrl = posters.thumbnailUrl?.let { buildStorageUrl(it) }

    return Release(
        id = id,
        name = names.main,
        nameEng = names.english,
        isInProduction = isInProduction,
        isOngoing = isOngoing,
        posterUrl = srcUrl,
        thumbnailUrl = thumbUrl,
        ageRatingLabel = ageRating.label,
        description = description,
        latestEpisodePublishTime = latestEpisode?.let { Instant.parse(it.updatedAt) }
    )
}
