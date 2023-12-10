package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.search.api.interactor.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.api.interactor.TrackInteractor
import com.example.playlistmaker.domain.search.models.Resource
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.search.models.SearchState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val trackInteractor: TrackInteractor,
) : ViewModel() {

    private var lastRequest = ""
    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        doRequest(lastRequest)
    }

    private val stateLiveData = MutableLiveData<SearchState>()

    fun observeState(): LiveData<SearchState> = stateLiveData

    fun searchDebounce(request: String) {
        lastRequest = request
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun atOnceRequest(request: String) {
        lastRequest = request
        doRequest(request)
        handler.removeCallbacks(searchRunnable)
    }

    fun reloadRequest(){
        CoroutineScope(Dispatchers.IO).launch{
            //Это чтобы было понятно что пользователь без интернета обновляет страницу
            delay(500)
            doRequest(lastRequest)
        }

    }

    private fun doRequest(text: String) {
        if (text.isNotEmpty()) {
            stateLiveData.postValue(SearchState.Loading)
            trackInteractor.searchTrack(text, object : TrackInteractor.TrackConsumer {
                override fun consume(foundTracksResource: List<Track>?, errorMessage: String?) {
                    CoroutineScope(Dispatchers.IO).launch{
                        if (foundTracksResource != null) {
                            Log.d("Response", foundTracksResource.toString())
                            if (foundTracksResource.isNotEmpty()) {
                                stateLiveData.postValue(SearchState.Content(foundTracksResource))
                            } else {
                                stateLiveData.postValue(SearchState.Empty)
                            }
                        } else if(errorMessage != null) {
                            stateLiveData.postValue(SearchState.Error(errorMessage))
                            lastRequest = text
                        }
                    }
                }
            })
        }
    }

    fun addToHistory(track: Track) {
        searchHistoryInteractor.addToTrackHistory(track)
    }

    fun getHistory(): List<Track> {
        return searchHistoryInteractor.getTrackHistory()
    }

    fun clearHistory(){
        searchHistoryInteractor.clearTrackHistory()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    Creator.provideSearchHistoryInteractor(),
                    Creator.provideTrackInteractor()
                )
            }
        }

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L

    }
}