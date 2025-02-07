package tech.bnuuy.anigiri.feature.favorites.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import tech.bnuuy.anigiri.core.network.exception.NotAuthorizedException
import tech.bnuuy.anigiri.feature.favorites.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.favorites.api.data.model.Release
import tech.bnuuy.anigiri.feature.favorites.api.data.repository.FavoritesRepository

class FetchFavoriteReleasesUseCaseImplTest {
    
    private lateinit var useCase: FetchFavoriteReleasesUseCaseImpl
    
    @MockK
    lateinit var repository: FavoritesRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = FetchFavoriteReleasesUseCaseImpl(repository)
    }
    
    @Test
    fun `on authorized call returns page`(): Unit = runBlocking {
        val page = 1
        val expected = mockk<PagedContent<Release>>()
        coEvery { repository.getFavoritesReleases(any()) } returns expected
        
        val result = useCase(page)
        
        assertEquals(result, expected)
        coVerify { repository.getFavoritesReleases(page) }
    }
    
    @Test
    fun `on unauthorized call throws NotAuthorizedException`(): Unit = runBlocking {
        val page = 1
        coEvery { repository.getFavoritesReleases(any()) } throws NotAuthorizedException()

        assertThrows(NotAuthorizedException::class.java) {
            runBlocking { useCase(page) }
        }
        
        coVerify { repository.getFavoritesReleases(page) }
    }
}
