package tech.bnuuy.anigiri.feature.release.data.model

import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType
import tech.bnuuy.anigiri.feature.release.api.data.model.ICollectionReleaseType

data class CollectionReleaseType(
    val authorized: Boolean = true,
    val type: CollectionType? = null,
) : ICollectionReleaseType()
