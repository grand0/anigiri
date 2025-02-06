package tech.bnuuy.anigiri.feature.home.data.mapper

import tech.bnuuy.anigiri.core.network.datasource.response.ProfileResponse
import tech.bnuuy.anigiri.core.network.util.buildStorageUrl

fun ProfileResponse.extractAvatarUrl(): String? {
    return avatar.preview?.let {
        buildStorageUrl(it)
    }
}
