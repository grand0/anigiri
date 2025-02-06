package tech.bnuuy.anigiri.core.db.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.bnuuy.anigiri.core.db.dao.SearchQueryDao
import tech.bnuuy.anigiri.core.db.entity.SearchQueryEntity

class SearchQueryDataSource internal constructor(
    private val searchQueryDao: SearchQueryDao,
) {
    suspend fun getAll(): List<SearchQueryEntity> = withContext(Dispatchers.IO) {
        searchQueryDao.getAll()
    }
    
    suspend fun insert(query: SearchQueryEntity) = withContext(Dispatchers.IO) {
        searchQueryDao.insert(query)
    }
    
    suspend fun clearAll() = withContext(Dispatchers.IO) {
        searchQueryDao.clearAll()
    }
}
