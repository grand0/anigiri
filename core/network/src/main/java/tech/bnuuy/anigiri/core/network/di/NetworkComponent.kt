package tech.bnuuy.anigiri.core.network.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.bnuuy.anigiri.core.network.session.AppSession

internal object NetworkComponent : KoinComponent {
    val appSession: AppSession by inject()
}
