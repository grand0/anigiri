package tech.bnuuy.anigiri.feature.search.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import tech.bnuuy.anigiri.feature.search.api.data.repository.CatalogRepository

class FetchYearsUseCaseImplTest {
    
    private lateinit var useCase: FetchYearsUseCaseImpl
    
    @MockK
    lateinit var repository: CatalogRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = FetchYearsUseCaseImpl(repository)
    }
    
    @Test
    fun `on call returns years`(): Unit = runBlocking {
        val expected = listOf(2023)
        coEvery { repository.catalogYears() } returns expected 
        
        val result = useCase()
        
        assertEquals(result, expected)
        coVerify { repository.catalogYears() }
    }
}
