package tech.bnuuy.anigiri.feature.home.data.mapper

import tech.bnuuy.anigiri.core.network.datasource.response.MetaContentResponse

internal fun <T> MetaContentResponse<T>.extractData(): List<T> {
    return data
}
