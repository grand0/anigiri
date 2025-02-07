package tech.bnuuy.anigiri.feature.home.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import tech.bnuuy.anigiri.feature.home.api.data.repository.ProfileRepository

class GetUserAvatarUrlUseCaseImplTest {
    
    private lateinit var useCase: GetUserAvatarUrlUseCaseImpl
    
    @MockK
    lateinit var repository: ProfileRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetUserAvatarUrlUseCaseImpl(repository)
    }
    
    @Test
    fun `on authorized call returns url`(): Unit = runBlocking {
        val expected = "https://example.org/avatar.jpg"
        coEvery { repository.getProfileAvatarUrl() } returns expected
        
        val result = useCase()
        
        assertEquals(result, expected)
        coVerify { repository.getProfileAvatarUrl() }
    }

    @Test
    fun `on unauthorized call returns null`(): Unit = runBlocking {
        coEvery { repository.getProfileAvatarUrl() } returns null

        val result = useCase()

        assertNull(result)
        coVerify { repository.getProfileAvatarUrl() }
    }
}
