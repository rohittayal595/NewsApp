package assignment.tokopedia.newsapp.model.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import assignment.tokopedia.newsapp.model.NewsSource
import assignment.tokopedia.newsapp.model.NewsSourceList
import assignment.tokopedia.newsapp.model.Resource
import assignment.tokopedia.newsapp.model.Status
import assignment.tokopedia.newsapp.model.network.NewsApi
import assignment.tokopedia.newsapp.model.room.AppDatabase
import assignment.tokopedia.newsapp.model.room.NewsSourceDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SourcesRepository(context: Context, private val coroutineScope: CoroutineScope) {
    private val newsSourceDao: NewsSourceDao by lazy {
        AppDatabase.getDatabase(context).newsSourceDao()
    }

    val LOG = SourcesRepository::class.java.simpleName

    private val newsApi: NewsApi? by lazy { NewsApi.getNewsService() }

    private val newsSourceListObservable: MutableLiveData<Resource<List<NewsSource>>> =
        MutableLiveData()

    fun getNewsSourceListObservable() = newsSourceListObservable

    fun fetchNewsSources() {
        newsSourceListObservable.value = Resource.loading(newsSourceListObservable.value?.data)
        loadAllNewsSourceFromDB()
        getNewsSourcesFromWeb()
    }

    private fun getNewsSourcesFromWeb() {
        newsApi?.getNewsSources()
            ?.enqueue(object : Callback<NewsSourceList> {
                override fun onResponse(
                    call: Call<NewsSourceList>,
                    response: Response<NewsSourceList>
                ) {
                    if (response.isSuccessful) {
                        newsSourceListObservable.value =
                            Resource.success(newsSourceListObservable.value?.data)
                        addNewsSourcesToDB(response.body()?.newsSourceList)
                    } else {
                        newsSourceListObservable.value = Resource.error(
                            response.code().toString(),
                            newsSourceListObservable.value?.data
                        )
                        when (response.code()) {
                            404 -> Log.e(LOG, "getNewsSourceFromWeb:: Error: 404")
                            500 -> Log.e(LOG, "getNewsSourceFromWeb:: Error: 500")
                            else -> Log.e(
                                LOG,
                                "getNewsSourceFromWeb:: Error: ${response.code()}"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<NewsSourceList>, t: Throwable) {
                    Log.e(LOG, t.message ?: "UnknownError")
                    newsSourceListObservable.value =
                        Resource.error(
                            t.message ?: "Unknown Error",
                            newsSourceListObservable.value?.data
                        )
                }
            })
    }

    private fun addNewsSourcesToDB(list: List<NewsSource>?) = coroutineScope.launch {
        var needsUpdate = false
        withContext(Dispatchers.IO) {
            if (list != null) {
                for (item in list) {
                    val inserted: Int = newsSourceDao.insertNewsSource(item).toInt()
                    if (inserted == -1) {
                        val updated: Int = newsSourceDao.updateNewsSource(item)
                        if (updated > 0) needsUpdate = true

                    } else {
                        needsUpdate = true
                    }
                }
            }
        }
        if (needsUpdate)
            loadAllNewsSourceFromDB()
    }

    private fun loadAllNewsSourceFromDB() = coroutineScope.launch {
        val resultsList = withContext(Dispatchers.IO) {
            newsSourceDao.getAllNewsSources()
        }
        newsSourceListObservable.value = when (newsSourceListObservable.value?.status) {
            Status.SUCCESS -> Resource.success(resultsList)
            Status.ERROR -> Resource.error(null.toString(), resultsList)
            else -> Resource.loading(resultsList)
        }

    }

}

