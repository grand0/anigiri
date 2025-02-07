package tech.bnuuy.anigiri.feature.release.usecase

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
import tech.bnuuy.anigiri.feature.release.api.data.model.Release
import tech.bnuuy.anigiri.feature.release.api.data.repository.ProfileRepository

class CheckFavoriteReleaseUseCaseImplTest {

    private lateinit var useCase: CheckFavoriteReleaseUseCaseImpl

    @MockK
    lateinit var repository: ProfileRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = CheckFavoriteReleaseUseCaseImpl(repository)
    }

    @Test
    fun `on authorized call returns boolean`(): Unit = runBlocking() {
        val favoriteRelease = mockk<Release>()
        val notFavoriteRelease = mockk<Release>()
        coEvery { repository.checkFavoriteRelease(favoriteRelease) } returns true
        coEvery { repository.checkFavoriteRelease(notFavoriteRelease) } returns false

        val result1 = useCase(favoriteRelease)
        val result2 = useCase(notFavoriteRelease)

        assertTrue(result1)
        assertFalse(result2)

        coVerify { repository.checkFavoriteRelease(favoriteRelease) }
        coVerify { repository.checkFavoriteRelease(notFavoriteRelease) }
    }

    @Test
    fun `on unauthorized call throws NotAuthorizedException`(): Unit = runBlocking {
        coEvery { repository.checkFavoriteRelease(any()) } throws NotAuthorizedException()
        
        assertThrows(NotAuthorizedException::class.java) {
            runBlocking { useCase(mockk()) }
        }
        
        coVerify { repository.checkFavoriteRelease(any()) }
    }
}
