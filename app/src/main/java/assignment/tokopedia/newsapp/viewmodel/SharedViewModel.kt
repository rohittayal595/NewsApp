package assignment.tokopedia.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val searchStringObservable = MutableLiveData<String>()

    fun searchUpdated(string: String) {
        searchStringObservable.value = string
    }

    fun getSearchStringObservable(): LiveData<String> = searchStringObservable
}