package tech.bnuuy.anigiri.feature.release.usecase

import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import tech.bnuuy.anigiri.core.network.exception.NotAuthorizedException
import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.api.data.repository.ProfileRepository

class RemoveFromFavoritesUseCaseImplTest {
    
    private lateinit var useCase: RemoveFromFavoritesUseCaseImpl
    
    @MockK
    lateinit var repository: ProfileRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = RemoveFromFavoritesUseCaseImpl(repository)
    }
    
    @Test
    fun `on authorized call calls repository removeFavoriteRelease`(): Unit = runBlocking {
        val release = mockk<Release>()
        coEvery { repository.removeFavoriteRelease(any()) } just Runs
        
        useCase(release)
        
        coVerify { repository.removeFavoriteRelease(release) }
    }
    
    @Test
    fun `on unauthorized call throws NotAuthorizedException`(): Unit = runBlocking {
        val release = mockk<Release>()
        coEvery { repository.removeFavoriteRelease(any()) } throws NotAuthorizedException()
        
        assertThrows(NotAuthorizedException::class.java) {
            runBlocking { useCase(release) }
        }

        coVerify { repository.removeFavoriteRelease(release) }
    }
}
