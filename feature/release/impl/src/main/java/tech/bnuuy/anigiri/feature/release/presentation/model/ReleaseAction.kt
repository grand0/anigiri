package tech.bnuuy.anigiri.feature.release.presentation.model

import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType

sealed interface ReleaseAction {
    data object Refresh : ReleaseAction
    data object AddToFavorites : ReleaseAction
    data object RemoveFromFavorites : ReleaseAction
    data class AddToCollection(val type: CollectionType) : ReleaseAction
    data object RemoveFromCollections : ReleaseAction
}
