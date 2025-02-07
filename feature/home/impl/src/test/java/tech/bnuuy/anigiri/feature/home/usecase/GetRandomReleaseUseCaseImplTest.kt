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
import tech.bnuuy.anigiri.feature.home.api.data.repository.ReleaseRepository

class GetRandomReleaseUseCaseImplTest {
    
    private lateinit var useCase: GetRandomReleaseUseCaseImpl
    
    @MockK
    lateinit var repository: ReleaseRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetRandomReleaseUseCaseImpl(repository)
    }
    
    @Test
    fun `on call returns release`(): Unit = runBlocking {
        val expected = mockk<Release>()
        coEvery { repository.getRandomRelease() } returns expected

        val result = useCase()

        assertEquals(result, expected)
        coVerify { repository.getRandomRelease() }
    }
}
