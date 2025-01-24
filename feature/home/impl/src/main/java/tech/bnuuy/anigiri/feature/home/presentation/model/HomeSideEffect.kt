package tech.bnuuy.anigiri.feature.home.presentation.model

internal sealed interface HomeSideEffect {
    data class ShowToast(val num: Int) : HomeSideEffect
}
