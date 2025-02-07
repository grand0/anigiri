package tech.bnuuy.anigiri.feature.home.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import tech.bnuuy.anigiri.feature.home.api.data.model.Release
import tech.bnuuy.anigiri.feature.home.api.data.repository.ReleaseRepository
import kotlin.time.Duration.Companion.hours

class GetLatestReleasesUseCaseImplTest {
    
    private lateinit var useCase: GetLatestReleasesUseCaseImpl
    
    @MockK
    lateinit var repository: ReleaseRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetLatestReleasesUseCaseImpl(repository)
    }
    
    @Test
    fun `on call returns releases`(): Unit = runBlocking {
        val instant = Instant.fromEpochMilliseconds(1735678800000)
        val release1 = mockk<Release>()
        coEvery { release1.latestEpisodePublishTime } returns instant
        val release2 = mockk<Release>()
        coEvery { release2.latestEpisodePublishTime } returns instant.plus(1.hours)
        val initial = listOf(release1, release2)
        val expected = initial.sortedByDescending { it.latestEpisodePublishTime }
        coEvery { repository.getLatestReleases() } returns initial

        val result = useCase()

        assertEquals(result, expected)
        coVerify { repository.getLatestReleases() }
    }
}
