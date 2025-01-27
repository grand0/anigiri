package tech.bnuuy.anigiri.feature.home.presentation.model

import tech.bnuuy.anigiri.feature.home.api.data.model.Release

internal sealed interface HomeSideEffect {
    data class ShowError(val error: Throwable?) : HomeSideEffect
    data class GoToRelease(val release: Release) : HomeSideEffect
}
