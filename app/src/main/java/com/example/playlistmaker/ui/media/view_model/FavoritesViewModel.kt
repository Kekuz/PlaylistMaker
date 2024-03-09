package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favorites.api.interactor.FavoritesInteractor
import com.example.playlistmaker.ui.media.models.FavoritesState
import com.example.playlistmaker.ui.search.models.SearchState
import com.example.playlistmaker.ui.util.Debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = stateLiveData

    private val debounce = Debounce()

    fun clickDebounce(): Boolean = debounce.clickDebounce()


    fun showFavorites() {
        viewModelScope.launch {
            favoritesInteractor.getFavorites().flowOn(Dispatchers.IO).map { list ->
                list.map { track ->
                    track.apply { isFavorite = true }
                }
            }.collect {
                if (it.isEmpty()) {
                    stateLiveData.postValue(FavoritesState.Empty)
                } else {
                    stateLiveData.postValue(FavoritesState.Content(it))
                }

            }
        }
    }
}