package tech.bnuuy.anigiri.feature.collections.presentation

import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType

sealed interface CollectionsAction {
    data class FetchCollection(val type: CollectionType) : CollectionsAction
}
