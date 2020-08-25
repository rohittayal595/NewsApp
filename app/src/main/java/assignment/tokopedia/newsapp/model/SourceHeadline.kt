package assignment.tokopedia.newsapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "source_headline_table",
    indices = [Index(value = ["url"], unique = true)]
)
data class SourceHeadline(
    @PrimaryKey(autoGenerate = true)
    val headlineId: Long = 0L,

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    val title: String = "",

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    val description: String = "",

    @SerializedName("url")
    @Expose
    @ColumnInfo(name = "url")
    val url: String = "",

    @SerializedName("urlToImage")
    @Expose
    @ColumnInfo(name = "url_to_Image")
    val urlToImage: String = "",

    @ColumnInfo(name = "source")
    var sourceId: String
)

data class SourceHeadlineList(
    @SerializedName("articles")
    val sourceHeadlineList: List<SourceHeadline>
)
