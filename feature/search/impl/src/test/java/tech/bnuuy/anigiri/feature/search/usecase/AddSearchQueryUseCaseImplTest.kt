package tech.bnuuy.anigiri.feature.search.usecase

import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import tech.bnuuy.anigiri.feature.search.api.data.model.SearchQuery
import tech.bnuuy.anigiri.feature.search.api.data.repository.SearchQueryRepository

class AddSearchQueryUseCaseImplTest {
    
    private lateinit var useCase: AddSearchQueryUseCaseImpl
    
    @MockK
    lateinit var repository: SearchQueryRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = AddSearchQueryUseCaseImpl(repository)
    }
    
    @Test
    fun `on call calls repository insert`(): Unit = runBlocking {
        val query = mockk<SearchQuery>()
        coEvery { repository.insert(any()) } just Runs
        
        useCase(query)
        
        coVerify { repository.insert(query) }
    }
}
