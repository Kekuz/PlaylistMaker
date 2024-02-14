package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.api.interactor.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.interactor.TrackInteractor
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.models.SearchState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val trackInteractor: TrackInteractor,
) : ViewModel() {

    private var lastRequest = ""
    private var isClickAllowed = true

    private var searchJob: Job? = null

    private val stateLiveData = MutableLiveData<SearchState>()

    fun observeState(): LiveData<SearchState> = stateLiveData

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }

    fun searchDebounce(request: String) {
        if (lastRequest != request) {// это чтобы когда мы возвращались на фрагмент, он заново не искал
            lastRequest = request
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                doRequest(lastRequest)
            }
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch{
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun atOnceRequest(request: String) {
        lastRequest = request
        doRequest(request)
        searchJob?.cancel()
    }

    fun reloadRequest() {
        CoroutineScope(Dispatchers.IO).launch {
            //Это чтобы было понятно что пользователь без интернета обновляет страницу
            delay(500)
            doRequest(lastRequest)
        }

    }

    private fun doRequest(text: String) {
        if (text.isNotEmpty()) {
            stateLiveData.postValue(SearchState.Loading)

            viewModelScope.launch {
                trackInteractor.searchTrack(text).collect {
                    val foundTracksResource = it.first
                    val errorMessage = it.second
                    if (foundTracksResource != null) {
                        Log.d("Response", foundTracksResource.toString())
                        if (foundTracksResource.isNotEmpty()) {
                            stateLiveData.postValue(SearchState.Content(foundTracksResource))
                        } else {
                            stateLiveData.postValue(SearchState.Empty)
                        }
                    } else if (errorMessage != null) {
                        stateLiveData.postValue(SearchState.Error(errorMessage))
                        lastRequest = text
                    }
                }
            }
        }
    }

    fun addToHistory(track: Track) {
        searchHistoryInteractor.addToTrackHistory(track)
    }

    fun getHistory(): List<Track> {
        return searchHistoryInteractor.getTrackHistory()
    }

    fun clearHistory() {
        searchHistoryInteractor.clearTrackHistory()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L

    }
}