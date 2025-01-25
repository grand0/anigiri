package tech.bnuuy.anigiri.feature.home.presentation.model

import tech.bnuuy.anigiri.core.network.model.Release

internal sealed interface HomeSideEffect {
    data class ShowError(val error: Throwable?) : HomeSideEffect
    data class GoToRelease(val release: Release) : HomeSideEffect
}
