package tech.bnuuy.anigiri.feature.release.presentation

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import tech.bnuuy.anigiri.feature.release.api.usecase.GetReleaseUseCase
import tech.bnuuy.anigiri.feature.release.presentation.model.ReleaseState

class ReleaseViewModel(
    val releaseId: Int,
    val getReleaseUseCase: GetReleaseUseCase,
) : ViewModel(), ContainerHost<ReleaseState, Nothing> {
    override val container = container<ReleaseState, Nothing>(ReleaseState())
    
    init {
        getRelease()
    }
    
    private fun getRelease() = intent {
        stateLoading()
        
        val result = runCatching { 
            getReleaseUseCase(releaseId)
        }
        reduce {
            state.copy(
                release = result.getOrNull(),
                isLoading = false,
                error = result.exceptionOrNull(),
            )
        }
    }
    
    @OptIn(OrbitExperimental::class)
    private suspend fun stateLoading() = subIntent { 
        reduce { 
            state.copy(
                release = null,
                isLoading = true,
                error = null,
            )
        }
    }
}
