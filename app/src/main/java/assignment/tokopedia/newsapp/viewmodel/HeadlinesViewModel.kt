package assignment.tokopedia.newsapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import assignment.tokopedia.newsapp.model.Resource
import assignment.tokopedia.newsapp.model.SourceHeadline
import assignment.tokopedia.newsapp.model.repository.HeadlineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HeadlinesViewModel(application: Application) : AndroidViewModel(application) {
    private val LOG = HeadlinesViewModel::class.java.simpleName
    private val mRepository: HeadlineRepository by lazy {
        HeadlineRepository(
            application,
            viewModelScope
        )
    }

    private val sourceHeadlineObservable: MediatorLiveData<Resource<List<SourceHeadline>>> =
        MediatorLiveData()

    private val searchHeadlinesObservable = MutableLiveData<List<SourceHeadline>>()

    init {
        sourceHeadlineObservable.addSource(mRepository.getSourceHeadlineListObservable()) { value ->
            sourceHeadlineObservable.value = value
        }
    }

    fun fetchNewsHeadlines(sourceId: String) {
        mRepository.fetchSourceHeadlines(sourceId)
    }

    fun getSourceHeadlineListObservable(): LiveData<Resource<List<SourceHeadline>>> =
        sourceHeadlineObservable

    fun getSearchHeadlineListObservable(): LiveData<List<SourceHeadline>> =
        searchHeadlinesObservable

    fun searchString(it: String?) {
        Log.e(LOG, it ?: "null")
        var filteredResults = sourceHeadlineObservable.value?.data
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                if (it != null) {
                    if (it.isNotEmpty()) {
                        filteredResults = filteredResults?.filter { source ->
                            source.title.toLowerCase(Locale.getDefault())
                                .contains(it.toLowerCase(Locale.getDefault())) ||
                                    source.description.toLowerCase(Locale.getDefault())
                                        .contains(it.toLowerCase(Locale.getDefault()))
                        }
                    }
                }
            }
            searchHeadlinesObservable.postValue(filteredResults)
        }
    }
}