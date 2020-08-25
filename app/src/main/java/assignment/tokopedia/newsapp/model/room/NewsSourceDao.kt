package assignment.tokopedia.newsapp.model.room

import androidx.room.*
import assignment.tokopedia.newsapp.model.NewsSource


@Dao
interface NewsSourceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNewsSource(newsSource: NewsSource): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateNewsSource(newsSource: NewsSource): Int

    @Query("SELECT * FROM news_source_table ORDER BY source_name DESC")
    fun getAllNewsSources(): List<NewsSource>
}