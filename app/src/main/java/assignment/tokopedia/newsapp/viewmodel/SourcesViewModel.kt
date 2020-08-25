package assignment.tokopedia.newsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import assignment.tokopedia.newsapp.model.NewsSource
import assignment.tokopedia.newsapp.model.Resource
import assignment.tokopedia.newsapp.model.Status
import assignment.tokopedia.newsapp.model.repository.SourcesRepository

class SourcesViewModel(application: Application) : AndroidViewModel(application) {
    private val mSourcesRepository: SourcesRepository by lazy {
        SourcesRepository(
            application,
            viewModelScope
        )
    }

    private val newsSourceObservable: MediatorLiveData<Resource<List<NewsSource>>> =
        MediatorLiveData()

    private var currentStatus: Status = Status.SUCCESS

    init {
        newsSourceObservable.addSource(mSourcesRepository.getNewsSourceListObservable()) { value ->
            if (currentStatus == value.status)
                newsSourceObservable.value = value
            currentStatus = value.status
        }
        mSourcesRepository.fetchNewsSources()
    }

    fun getNewsSourceListObservable(): LiveData<Resource<List<NewsSource>>> = newsSourceObservable
}