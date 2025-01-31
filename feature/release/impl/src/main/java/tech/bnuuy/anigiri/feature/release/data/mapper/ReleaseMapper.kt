package tech.bnuuy.anigiri.feature.release.data.mapper

import tech.bnuuy.anigiri.core.network.datasource.response.ReleaseResponse
import tech.bnuuy.anigiri.core.network.util.buildStorageUrl
import tech.bnuuy.anigiri.feature.release.api.data.model.Genre
import tech.bnuuy.anigiri.feature.release.api.data.model.Release

internal fun List<ReleaseResponse>.mapList(): List<Release> = map {
    it.toDomain()
}

internal fun ReleaseResponse.toDomain(): Release {
    val srcUrl = posters.srcUrl?.let { buildStorageUrl(it) }
    val thumbUrl = posters.thumbnailUrl?.let { buildStorageUrl(it) }

    return Release(
        id = id,
        name = names.main,
        nameEng = names.english,
        year = year,
        isInProduction = isInProduction,
        isOngoing = isOngoing,
        posterUrl = srcUrl,
        thumbnailUrl = thumbUrl,
        ageRatingLabel = ageRating.label,
        description = description,
        episodesTotal = episodesTotal,
        episodeDurationMinutes = episodeDurationMinutes,
        genres = genres?.map { Genre(id = it.id, name = it.name) },
        members = members?.groupBy(
            keySelector = { it.role.name },
            valueTransform = { it.name },
        ),
        episodes = episodes?.mapList(),
    )
}
