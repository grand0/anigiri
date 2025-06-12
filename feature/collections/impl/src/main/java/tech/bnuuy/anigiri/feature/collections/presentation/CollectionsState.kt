package tech.bnuuy.anigiri.feature.collections.presentation

import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType

data class CollectionsState(
    val collectionType: CollectionType = CollectionType.PLANNED,
    val totalItems: Int = 0,
)