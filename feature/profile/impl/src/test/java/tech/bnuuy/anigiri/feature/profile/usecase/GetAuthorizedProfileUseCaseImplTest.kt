package tech.bnuuy.anigiri.feature.profile.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import tech.bnuuy.anigiri.feature.profile.api.data.model.Profile
import tech.bnuuy.anigiri.feature.profile.api.data.repository.ProfileRepository

class GetAuthorizedProfileUseCaseImplTest {
    
    private lateinit var useCase: GetAuthorizedProfileUseCaseImpl
    
    @MockK
    lateinit var repository: ProfileRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetAuthorizedProfileUseCaseImpl(repository)
    }
    
    @Test
    fun `on authorized call returns profile`(): Unit = runBlocking {
        val expected = mockk<Profile>()
        coEvery { repository.getAuthorizedProfile(useCache = any()) } returns expected
        
        val result = useCase()
        
        assertEquals(result, expected)
        coVerify { repository.getAuthorizedProfile(useCache = any()) }
    }

    @Test
    fun `on unauthorized call returns null`(): Unit = runBlocking {
        coEvery { repository.getAuthorizedProfile(useCache = any()) } returns null

        val result = useCase()

        assertNull(result)
        coVerify { repository.getAuthorizedProfile(useCache = any()) }
    }
}
