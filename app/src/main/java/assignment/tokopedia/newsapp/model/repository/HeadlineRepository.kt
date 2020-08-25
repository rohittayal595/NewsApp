package assignment.tokopedia.newsapp.model.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import assignment.tokopedia.newsapp.model.Resource
import assignment.tokopedia.newsapp.model.SourceHeadline
import assignment.tokopedia.newsapp.model.SourceHeadlineList
import assignment.tokopedia.newsapp.model.Status
import assignment.tokopedia.newsapp.model.network.NewsApi
import assignment.tokopedia.newsapp.model.room.AppDatabase
import assignment.tokopedia.newsapp.model.room.SourceHeadlineDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeadlineRepository(context: Context, private val coroutineScope: CoroutineScope) {

    private val LOG = HeadlineRepository::class.java.simpleName

    private val sourceHeadlineDao: SourceHeadlineDao by lazy {
        AppDatabase.getDatabase(context).sourceHeadlineDao()
    }

    private val newsApi: NewsApi? by lazy { NewsApi.getNewsService() }

    private val sourceHeadlineListObservable: MutableLiveData<Resource<List<SourceHeadline>>> =
        MutableLiveData()

    fun getSourceHeadlineListObservable() = sourceHeadlineListObservable

    fun fetchSourceHeadlines(sourceId: String) {
        sourceHeadlineListObservable.value =
            Resource.loading(sourceHeadlineListObservable.value?.data)
        loadAllSourceHeadlineFromDB(sourceId)
        getSourceHeadlinesFromWeb(sourceId)
    }

    private fun getSourceHeadlinesFromWeb(sourceId: String) {
        newsApi?.getSourceHeadlines(sourceId)
            ?.enqueue(object : Callback<SourceHeadlineList> {
                override fun onResponse(
                    call: Call<SourceHeadlineList>,
                    response: Response<SourceHeadlineList>
                ) {
                    if (response.isSuccessful) {
                        sourceHeadlineListObservable.value =
                            Resource.success(sourceHeadlineListObservable.value?.data)
                        addSourceHeadlineToDB(response.body()?.sourceHeadlineList, sourceId)
                    } else {
                        sourceHeadlineListObservable.value = Resource.error(
                            response.code().toString(),
                            sourceHeadlineListObservable.value?.data
                        )
                        when (response.code()) {
                            404 -> Log.e(LOG, "getSourceHeadlinesFromWeb:: Error: 404")
                            500 -> Log.e(LOG, "getSourceHeadlinesFromWeb:: Error: 500")
                            else -> Log.e(
                                LOG,
                                "getSourceHeadlinesFromWeb:: Error: ${response.code()}"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<SourceHeadlineList>, t: Throwable) {
                    Log.e(LOG, t.message ?: "UnknownError")
                    sourceHeadlineListObservable.value =
                        Resource.error(
                            t.message ?: "Unknown Error",
                            sourceHeadlineListObservable.value?.data
                        )
                }
            })
    }

    private fun addSourceHeadlineToDB(list: List<SourceHeadline>?, sourceId: String) =
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                sourceHeadlineDao.clearAllSourceHeadlines(sourceId)
                if (list != null) {
                    for (item in list) {
                        item.sourceId = sourceId
                        val inserted: Int = sourceHeadlineDao.insertSourceHeadline(item).toInt()
                        if (inserted == -1)
                            sourceHeadlineDao.updateSourceHeadline(item)
                    }
                }
            }
            loadAllSourceHeadlineFromDB(sourceId)
        }

    private fun loadAllSourceHeadlineFromDB(sourceId: String) = coroutineScope.launch {
        val resultsList = withContext(Dispatchers.IO) {
            sourceHeadlineDao.getAllSourceHeadlines(sourceId)
        }
        sourceHeadlineListObservable.value = when (sourceHeadlineListObservable.value?.status) {
            Status.SUCCESS -> Resource.success(resultsList)
            Status.ERROR -> Resource.error(null.toString(), resultsList)
            else -> Resource.loading(resultsList)
        }

    }

}
