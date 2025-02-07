package tech.bnuuy.anigiri.feature.release.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.api.data.repository.ReleaseRepository

class GetReleaseUseCaseImplTest {
    
    private lateinit var useCase: GetReleaseUseCaseImpl
    
    @MockK
    lateinit var repository: ReleaseRepository
    
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetReleaseUseCaseImpl(repository)
    }
    
    @Test
    fun `on call returns release`(): Unit = runBlocking {
        val id = 1
        val expected = mockk<Release>()
        coEvery { repository.getRelease(id) } returns expected
        
        val result = useCase(id)
        
        assertEquals(result, expected)
        coEvery { repository.getRelease(id) }
    }
}
