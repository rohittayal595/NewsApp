package assignment.tokopedia.newsapp.model.network

import assignment.tokopedia.newsapp.BuildConfig
import assignment.tokopedia.newsapp.model.NewsSourceList
import assignment.tokopedia.newsapp.model.SourceHeadlineList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApi {

    @GET("/v2/sources?language=en&apiKey=${BuildConfig.NEWS_API_KEY}")
    fun getNewsSources(): Call<NewsSourceList>

    @GET("/v2/top-headlines?apiKey=${BuildConfig.NEWS_API_KEY}")
    fun getSourceHeadlines(@Query("sources") sourceId: String): Call<SourceHeadlineList>

    companion object {
        private const val BASE_URL = "https://newsapi.org/"

        @Volatile
        private var INSTANCE: NewsApi? = null

        fun getNewsService(): NewsApi {
            return INSTANCE ?: synchronized(this) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val instance = retrofit.create(NewsApi::class.java)
                INSTANCE = instance
                instance
            }
        }
    }
}