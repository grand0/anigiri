package tech.bnuuy.anigiri.feature.profile.presentation

internal sealed interface ProfileAction {
    data object Refresh : ProfileAction
    data class Login(val login: String, val password: String) : ProfileAction
    data object Logout : ProfileAction
}
