package tech.bnuuy.anigiri.feature.search.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import tech.bnuuy.anigiri.feature.search.api.data.model.Genre
import tech.bnuuy.anigiri.feature.search.api.data.repository.CatalogRepository

class FetchGenresUseCaseImplTest {
    
    private lateinit var useCase: FetchGenresUseCaseImpl
    
    @MockK
    lateinit var repository: CatalogRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = FetchGenresUseCaseImpl(repository)
    }
    
    @Test
    fun `on call returns genres`(): Unit = runBlocking {
        val expected = listOf(mockk<Genre>())
        coEvery { repository.catalogGenres() } returns expected 
        
        val result = useCase()
        
        assertEquals(result, expected)
        coVerify { repository.catalogGenres() }
    }
}
