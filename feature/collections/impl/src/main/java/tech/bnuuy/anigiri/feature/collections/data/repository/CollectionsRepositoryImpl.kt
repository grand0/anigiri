package tech.bnuuy.anigiri.feature.collections.data.repository

import tech.bnuuy.anigiri.core.network.datasource.AccountsDataSource
import tech.bnuuy.anigiri.core.network.datasource.response.ReleaseResponse
import tech.bnuuy.anigiri.feature.collections.api.data.model.ICollectionFilter
import tech.bnuuy.anigiri.feature.collections.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.collections.api.data.model.Release
import tech.bnuuy.anigiri.feature.collections.api.data.repository.CollectionsRepository
import tech.bnuuy.anigiri.feature.collections.data.mapper.toDomain
import tech.bnuuy.anigiri.feature.collections.data.mapper.toPagedContent
import tech.bnuuy.anigiri.feature.collections.data.model.CollectionFilter

class CollectionsRepositoryImpl(
    private val accountsDataSource: AccountsDataSource,
) : CollectionsRepository {

    override suspend fun getCollectionReleases(filter: ICollectionFilter): PagedContent<Release> {
        val filter = filter as CollectionFilter
        return accountsDataSource.collectionReleases(
            page = filter.page,
            collectionType = filter.collectionType,
        ).toPagedContent(ReleaseResponse::toDomain)
    }
}
