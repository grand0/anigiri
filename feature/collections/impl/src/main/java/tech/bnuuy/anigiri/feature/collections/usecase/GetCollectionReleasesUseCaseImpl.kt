package tech.bnuuy.anigiri.feature.collections.usecase

import tech.bnuuy.anigiri.feature.collections.api.data.model.ICollectionFilter
import tech.bnuuy.anigiri.feature.collections.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.collections.api.data.model.Release
import tech.bnuuy.anigiri.feature.collections.api.data.repository.CollectionsRepository
import tech.bnuuy.anigiri.feature.collections.api.usecase.GetCollectionReleasesUseCase

class GetCollectionReleasesUseCaseImpl(
    private val collectionsRepository: CollectionsRepository,
) : GetCollectionReleasesUseCase {
    override suspend fun invoke(filter: ICollectionFilter): PagedContent<Release> {
        return collectionsRepository.getCollectionReleases(filter)
    }
}
