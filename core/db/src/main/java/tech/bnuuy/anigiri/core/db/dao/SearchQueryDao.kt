package tech.bnuuy.anigiri.core.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.bnuuy.anigiri.core.db.entity.SearchQueryEntity

@Dao
internal interface SearchQueryDao {
    @Query("SELECT * FROM queries ORDER BY timestamp DESC")
    suspend fun getAll(): List<SearchQueryEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(query: SearchQueryEntity)
    
    @Query("DELETE FROM queries")
    suspend fun clearAll()
}
