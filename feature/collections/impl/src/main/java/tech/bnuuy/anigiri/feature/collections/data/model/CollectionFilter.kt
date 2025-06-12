package tech.bnuuy.anigiri.feature.collections.data.model

import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType
import tech.bnuuy.anigiri.feature.collections.api.data.model.ICollectionFilter

data class CollectionFilter(
    val page: Int,
    val collectionType: CollectionType,
) : ICollectionFilter()