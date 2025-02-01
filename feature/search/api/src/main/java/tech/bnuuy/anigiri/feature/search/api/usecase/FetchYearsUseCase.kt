package tech.bnuuy.anigiri.feature.search.api.usecase

interface FetchYearsUseCase {
    suspend operator fun invoke(): List<Int>
}
