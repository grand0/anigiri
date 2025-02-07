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
import tech.bnuuy.anigiri.feature.search.api.data.model.SearchQuery
import tech.bnuuy.anigiri.feature.search.api.data.repository.SearchQueryRepository

class GetAllSearchQueriesUseCaseImplTest {
    
    private lateinit var useCase: GetAllSearchQueriesUseCaseImpl
    
    @MockK
    lateinit var repository: SearchQueryRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetAllSearchQueriesUseCaseImpl(repository)
    }
    
    @Test
    fun `on call returns search queries`(): Unit = runBlocking {
        val expected = listOf(mockk<SearchQuery>())
        coEvery { repository.getAll() } returns expected
        
        val result = useCase()
        
        assertEquals(result, expected)
        coVerify { repository.getAll() }
    }
}
