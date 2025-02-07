package tech.bnuuy.anigiri.feature.home.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import tech.bnuuy.anigiri.feature.home.api.data.model.Release
import tech.bnuuy.anigiri.feature.home.api.data.repository.ProfileRepository

class GetFavoriteReleasesUseCaseImplTest {
    
    private lateinit var useCase: GetFavoriteReleasesUseCaseImpl
    
    @MockK
    lateinit var repository: ProfileRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetFavoriteReleasesUseCaseImpl(repository)
    }
    
    @Test
    fun `on authorized call returns releases`(): Unit = runBlocking {
        val expected = listOf(mockk<Release>())
        coEvery { repository.getFavoriteReleases() } returns expected
        
        val result = useCase()
        
        assertEquals(result, expected)
        coVerify { repository.getFavoriteReleases() }
    }

    @Test
    fun `on unauthorized call returns empty list`(): Unit = runBlocking {
        val expected = emptyList<Release>()
        coEvery { repository.getFavoriteReleases() } returns expected

        val result = useCase()

        assertEquals(result, expected)
        coVerify { repository.getFavoriteReleases() }
    }
}
