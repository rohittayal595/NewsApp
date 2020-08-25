package assignment.tokopedia.newsapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "news_source_table",
    indices = [Index(value = ["source_name"], unique = true)]
)
data class NewsSource(

    @PrimaryKey(autoGenerate = true)
    val sourceId: Long = 0L,

    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "source_name_id")
    var sourceNameId: String = "",

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "source_name")
    var sourceName: String = "",

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "source_description")
    val sourceDescription: String = ""
)

data class NewsSourceList(
    @SerializedName("sources")
    val newsSourceList: List<NewsSource>
)
