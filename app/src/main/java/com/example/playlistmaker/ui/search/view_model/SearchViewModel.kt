package com.example.playlistmaker.ui.search.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.api.interactor.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.interactor.TrackInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.search.models.SearchState
import com.example.playlistmaker.ui.util.Debounce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val trackInteractor: TrackInteractor,
) : ViewModel() {

    private var searchQuery = ""

    private var searchJob: Job? = null

    private val stateLiveData = MutableLiveData<SearchState>()

    fun observeState(): LiveData<SearchState> = stateLiveData

    private val debounce = Debounce()


    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }

    fun searchDebounce(request: String) {
        if (searchQuery != request) {// это чтобы когда мы возвращались на фрагмент, он заново не искал
            searchQuery = request
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                doRequest(searchQuery)
            }
        }
    }

    fun clickDebounce(): Boolean = debounce.clickDebounce()


    fun atOnceRequest(request: String) {
        searchQuery = request
        doRequest(request)
        searchJob?.cancel()
    }

    fun reloadRequest() {
        CoroutineScope(Dispatchers.IO).launch {
            //Это чтобы было понятно что пользователь без интернета обновляет страницу
            delay(500)
            doRequest(searchQuery)
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
                        searchQuery = text
                    }
                }
            }
        }
    }

    suspend fun addToHistory(track: Track) {
        searchHistoryInteractor.addToTrackHistory(track)
    }

    suspend fun getHistory(): List<Track> {
        return searchHistoryInteractor.getTrackHistory()
    }

    fun clearHistory() {
        searchHistoryInteractor.clearTrackHistory()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}