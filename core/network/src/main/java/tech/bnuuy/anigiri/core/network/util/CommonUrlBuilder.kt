package tech.bnuuy.anigiri.core.network.util

import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.http.parseUrl
import tech.bnuuy.anigiri.core.network.BuildConfig

fun buildStorageUrl(pathSegments: String) =
    URLBuilder(parseUrl(BuildConfig.ANILIBRIA_STORAGE_BASE_URL)!!)
        .appendPathSegments(pathSegments)
        .toString()
