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
import tech.bnuuy.anigiri.feature.search.api.data.model.PagedContent
import tech.bnuuy.anigiri.feature.search.api.data.model.Release
import tech.bnuuy.anigiri.feature.search.api.data.repository.CatalogRepository

class SearchCatalogUseCaseImplTest {
    
    private lateinit var useCase: SearchCatalogUseCaseImpl
    
    @MockK
    lateinit var repository: CatalogRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = SearchCatalogUseCaseImpl(repository)
    }
    
    @Test
    fun `on call returns releases`(): Unit = runBlocking {
        val expected = mockk<PagedContent<Release>>()
        coEvery { repository.searchCatalog(any()) } returns expected
        
        val result = useCase(mockk())
        
        assertEquals(result, expected)
        coVerify { repository.searchCatalog(any()) }
    }
}
