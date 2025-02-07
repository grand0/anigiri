package tech.bnuuy.anigiri.feature.search.usecase

import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import tech.bnuuy.anigiri.feature.search.api.data.repository.SearchQueryRepository

class ClearSearchQueriesUseCaseImplTest {
    
    private lateinit var useCase: ClearSearchQueriesUseCaseImpl
    
    @MockK
    lateinit var repository: SearchQueryRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = ClearSearchQueriesUseCaseImpl(repository)
    }
    
    @Test
    fun `on call calls repository clearAll`(): Unit = runBlocking {
        coEvery { repository.clearAll() } just Runs
        
        useCase()
        
        coVerify { repository.clearAll() }
    }
}
