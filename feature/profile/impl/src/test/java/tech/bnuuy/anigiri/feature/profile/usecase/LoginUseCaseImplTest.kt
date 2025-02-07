package tech.bnuuy.anigiri.feature.profile.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import tech.bnuuy.anigiri.core.network.exception.WrongCredentialsException
import tech.bnuuy.anigiri.feature.profile.api.data.model.Profile
import tech.bnuuy.anigiri.feature.profile.api.data.repository.ProfileRepository

class LoginUseCaseImplTest {
    
    private lateinit var useCase: LoginUseCaseImpl
    
    @MockK
    lateinit var repository: ProfileRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = LoginUseCaseImpl(repository)
    }
    
    @Test
    fun `on valid call returns profile`(): Unit = runBlocking {
        val login = "qwerty"
        val password = "123456"
        val expected = mockk<Profile>()
        coEvery { repository.login(login = login, password = password) } returns expected
        
        val result = useCase(login = login, password = password)
        
        assertEquals(result, expected)
        coVerify { repository.login(login = login, password = password) }
    }
    
    @Test
    fun `on call with wrong credentials throws WrongCredentialsException`(): Unit = runBlocking {
        val login = "qwerty"
        val password = "654321"
        coEvery {
            repository.login(login = login, password = password)
        } throws WrongCredentialsException()
        
        assertThrows(WrongCredentialsException::class.java) {
            runBlocking { useCase(login = login, password = password) }
        }
        coVerify { repository.login(login = login, password = password) }
    }
}
