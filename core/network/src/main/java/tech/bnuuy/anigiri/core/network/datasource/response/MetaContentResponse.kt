package tech.bnuuy.anigiri.core.network.datasource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaContentResponse<T>(
    @SerialName("data")
    val data: List<T>,
    @SerialName("meta")
    val meta: Meta,
) {
    
    @Serializable
    data class Meta(
        @SerialName("pagination")
        val pagination: Pagination,
    ) {
        
        @Serializable
        data class Pagination(
            @SerialName("total")
            val total: Int,
            @SerialName("count")
            val count: Int,
            @SerialName("per_page")
            val perPage: Int,
            @SerialName("current_page")
            val currentPage: Int,
            @SerialName("total_pages")
            val totalPages: Int,
        )
    }
}
