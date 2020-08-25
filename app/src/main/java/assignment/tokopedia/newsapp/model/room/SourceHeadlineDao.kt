package assignment.tokopedia.newsapp.model.room

import androidx.room.*
import assignment.tokopedia.newsapp.model.SourceHeadline


@Dao
interface SourceHeadlineDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSourceHeadline(sourceHeadline: SourceHeadline): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateSourceHeadline(sourceHeadline: SourceHeadline)

    @Query("SELECT * FROM source_headline_table WHERE source=:sourceId ORDER BY title DESC")
    fun getAllSourceHeadlines(sourceId: String): List<SourceHeadline>

    @Query("DELETE FROM source_headline_table WHERE source=:sourceId")
    fun clearAllSourceHeadlines(sourceId: String)
}