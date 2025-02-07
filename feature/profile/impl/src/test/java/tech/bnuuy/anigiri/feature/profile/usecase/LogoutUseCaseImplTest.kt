package tech.bnuuy.anigiri.feature.profile.usecase

import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import tech.bnuuy.anigiri.core.network.exception.NotAuthorizedException
import tech.bnuuy.anigiri.feature.profile.api.data.repository.ProfileRepository

class LogoutUseCaseImplTest {
    
    private lateinit var useCase: LogoutUseCaseImpl
    
    @MockK
    lateinit var repository: ProfileRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = LogoutUseCaseImpl(repository)
    }
    
    @Test
    fun `on authorized call calls repository logout`(): Unit = runBlocking {
        coEvery { repository.logout() } just Runs
        
        useCase()
        
        coVerify { repository.logout() }
    }
    
    @Test
    fun `on unauthorized call throws NotAuthorizedException`(): Unit = runBlocking {
        coEvery { repository.logout() } throws NotAuthorizedException()
        
        assertThrows(NotAuthorizedException::class.java) {
            runBlocking { useCase() }
        }
        
        coVerify { repository.logout() }
    }
}
