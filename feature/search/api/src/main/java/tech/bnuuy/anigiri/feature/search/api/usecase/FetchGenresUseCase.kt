package tech.bnuuy.anigiri.feature.search.api.usecase

import tech.bnuuy.anigiri.feature.search.api.data.model.Genre

interface FetchGenresUseCase {
    suspend operator fun invoke(): List<Genre>
}
