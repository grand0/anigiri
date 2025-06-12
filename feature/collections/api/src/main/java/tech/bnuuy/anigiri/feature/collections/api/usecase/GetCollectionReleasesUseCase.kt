package tech.bnuuy.anigiri.feature.collections.api.usecase

import tech.bnuuy.anigiri.feature.collections.api.data.model.ICollectionFilter
import tech.bnuuy.anigiri.feature.collections.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.collections.api.data.model.Release

interface GetCollectionReleasesUseCase {
    suspend operator fun invoke(filter: ICollectionFilter): PagedContent<Release>
}
