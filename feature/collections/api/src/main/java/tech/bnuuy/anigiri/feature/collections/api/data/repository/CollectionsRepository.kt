package tech.bnuuy.anigiri.feature.collections.api.data.repository

import tech.bnuuy.anigiri.feature.collections.api.data.model.ICollectionFilter
import tech.bnuuy.anigiri.feature.collections.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.collections.api.data.model.Release

interface CollectionsRepository {
    suspend fun getCollectionReleases(filter: ICollectionFilter): PagedContent<Release>
}
