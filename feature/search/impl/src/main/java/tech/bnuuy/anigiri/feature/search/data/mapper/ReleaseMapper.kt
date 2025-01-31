package tech.bnuuy.anigiri.feature.search.data.mapper

import tech.bnuuy.anigiri.core.network.datasource.response.ReleaseResponse
import tech.bnuuy.anigiri.core.network.util.buildStorageUrl
import tech.bnuuy.anigiri.feature.search.api.data.model.Release

fun List<ReleaseResponse>.listToDomain(): List<Release> = map { it.toDomain() }

fun ReleaseResponse.toDomain(): Release {
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
        description = description
    )
}
