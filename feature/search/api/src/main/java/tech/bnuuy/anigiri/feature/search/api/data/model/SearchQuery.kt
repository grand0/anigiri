package tech.bnuuy.anigiri.feature.search.api.data.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class SearchQuery(
    val query: String,
    val timestamp: Instant = Clock.System.now(),
)
