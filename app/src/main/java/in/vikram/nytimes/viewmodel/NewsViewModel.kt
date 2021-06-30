package `in`.vikram.nytimes.viewmodel

import `in`.vikram.nytimes.data.DataSourceInterface
import `in`.vikram.nytimes.data.Injection
import `in`.vikram.nytimes.data.model.News
import `in`.vikram.nytimes.custom.Resource
import `in`.vikram.nytimes.custom.Status
import `in`.vikram.nytimes.custom.Status.ERROR
import `in`.vikram.nytimes.custom.Status.SUCCESS
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsViewModel : AndroidViewModel {

    private var dataRepository : DataSourceInterface

    private var searchJob: Job? = null
    private var textQuery = ""

    private var page = 0

    private val _news = MutableLiveData<Resource<MutableList<News>?>>()
    val news : LiveData<Resource<MutableList<News>?>> = _news

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    constructor(application: Application) : super(application) {
        dataRepository = Injection.provideDataRepository(application.applicationContext)
    }


    fun onSearchQueryChanged(query: String) {
        val newQuery = query.trim().takeIf { it.length >= 2 } ?: ""
        if (news.value?.data == null || news.value?.data?.isEmpty() == true) {
            textQuery = newQuery
            page = 0
            _news.value = Resource.loading()
            executeSearch()
        }
    }

    fun getNextPage() {
        if (textQuery.isEmpty() || isLoading.value == true) {
            return
        }

        _isLoading.value = true

        page++

        executeSearch()
    }

    private fun executeSearch() {
        // Cancel any in-flight searches
        searchJob?.cancel()

        if (textQuery.isEmpty()) {
            clearSearchResults()
            return
        }



        searchJob = viewModelScope.launch {
            // The user could be typing or toggling filters rapidly. Giving the search job
            // a slight delay and cancelling it on each call to this method effectively debounces.
            delay(200)

            val searchResult = dataRepository.getSearchResults(textQuery, page)
            processSearchResult(searchResult)
            _isLoading.value = false
        }
    }

    private fun clearSearchResults() {
        _news.value = Resource.success(ArrayList())
        // Explicitly set false to not show the "No results" state
        page = 0
    }

    private fun processSearchResult(searchResult: Resource<MutableList<News>?>) {
        if (searchResult.status == Status.LOADING) {
            return // avoids UI flickering
        }

        if (searchResult.status == ERROR) {
            if (page == 0) {
                _news.value = searchResult
            } else page--
            return
        }

        if (searchResult.status == SUCCESS) {
            if (page > 0) {
                searchResult.data?.let { _news.value?.data?.addAll(it) }
                _news.notifyObserver()
            } else _news.value = searchResult

        }

    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

}