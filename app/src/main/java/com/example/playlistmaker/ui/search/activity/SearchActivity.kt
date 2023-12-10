package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.models.SearchState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    //private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()

    private val onClick: (Track) -> Unit =
        {
            if (viewModel.clickDebounce()) {
                if (it.previewUrl != null) {
                    Log.e("Track", it.toString())
                    //searchHistoryInteractor.addToTrackHistory(it)
                    val intent = Intent(this, AudioPlayerActivity::class.java)
                    intent.putExtra("track", Gson().toJson(it))

                    Creator.initTrack(it)// Добавляем в креатор трек

                    startActivity(intent)
                    //historyAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Песня отсутсвует на сервере", Toast.LENGTH_LONG).show()
                }
            }
        }

    private val tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(tracks, onClick)

    //private val historyAdapter = TrackAdapter(searchHistoryInteractor.getTrackHistory(), onClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        binding.trackRv.adapter = trackAdapter
        //binding.historyRv.adapter = historyAdapter

        /*binding.clearHistoryBtn.setOnClickListener {
            searchHistoryInteractor.clearTrackHistory()
            binding.historySv.isVisible = false
        }*/

        binding.errorBtn.setOnClickListener {
            showEmpty()
            viewModel.reloadRequest()
        }

        binding.ivBackArrowBtn.setOnClickListener {
            finish()
        }

        binding.clearIv.setOnClickListener {
            binding.inputEt.setText("")
            showEmpty()

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

            binding.inputEt.clearFocus()
        }

        binding.inputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.doRequest(binding.inputEt.text.toString())
                viewModel.removeCallbacks()
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
                true
            }
            false
        }

        /*binding.inputEt.setOnFocusChangeListener { _, hasFocus ->
            binding.historySv.visibility =
                if (hasFocus && binding.inputEt.text.isEmpty() && searchHistoryInteractor.getTrackHistory().isNotEmpty())
                    View.VISIBLE
                else
                    View.GONE
        }*/

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showClearBnt(s)
                viewModel.searchDebounce(s.toString())
                /*if (binding.inputEt.hasFocus() && s?.isEmpty() == true *//*&& searchHistoryInteractor.getTrackHistory().isNotEmpty()*//*) {
                    binding.historySv.isVisible = true

                    binding.errorIv.isVisible = false
                    binding.errorTv.isVisible = false
                    binding.errorBtn.isVisible = false
                    tracks.clear()
                    trackAdapter.notifyDataSetChanged()
                } else {
                    binding.historySv.isVisible = false
                }*/
            }
        }

        binding.inputEt.addTextChangedListener(simpleTextWatcher)

        viewModel.observeState().observe(this) {
            render(it)
        }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Empty -> showEmptyContent()
            is SearchState.Error -> showError(state.errorMessage)
            is SearchState.Loading -> showLoading()
        }
    }

    private fun showContent(content: List<Track>) {
        //Это чтобы при повороте почистить ресайклер, а то че он
        if (binding.inputEt.text.toString() != "") {
            binding.searchPb.isVisible = false
            binding.errorIv.isVisible = false
            binding.errorTv.isVisible = false
            binding.errorBtn.isVisible = false
            tracks.clear()
            tracks.addAll(content)
            trackAdapter.notifyDataSetChanged()
            binding.trackRv.isVisible = true
        }
    }

    private fun showEmptyContent() {
        //Это чтобы при повороте почистить ресайклер, а то че он
        if (binding.inputEt.text.toString() != "") {
            binding.searchPb.isVisible = false
            binding.trackRv.isVisible = false

            binding.errorIv.setBackgroundResource(R.drawable.not_found_icon)
            binding.errorTv.text = getString(R.string.nothing_found)
            binding.errorIv.isVisible = true
            binding.errorTv.isVisible = true
        }
    }

    private fun showError(errorMessage: String) {
        //Это чтобы при повороте почистить ресайклер, а то че он
        if(binding.inputEt.text.toString() != "") {
            binding.searchPb.isVisible = false
            binding.trackRv.isVisible = false

            binding.errorIv.setBackgroundResource(R.drawable.internet_problem_icon)
            binding.errorTv.text = errorMessage
            binding.errorBtn.isVisible = true
            binding.errorIv.isVisible = true
            binding.errorTv.isVisible = true
        }

    }

    private fun showLoading() {
        binding.searchPb.isVisible = true
    }

    private fun showClearBnt(s: CharSequence?) {
        binding.clearIv.isVisible = !s.isNullOrEmpty()
    }

    private fun showEmpty() {
        binding.trackRv.isVisible = false
        binding.errorIv.isVisible = false
        binding.errorBtn.isVisible = false
        binding.errorTv.isVisible = false
        binding.searchPb.isVisible = false
    }

    /*override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_DATA, editTextData)
        outState.putString(LAST_RESPONSE_DATA, lastResponse)

    }*/

    /*override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextData = savedInstanceState.getString(EDIT_TEXT_DATA, "")
        lastResponse = savedInstanceState.getString(LAST_RESPONSE_DATA, "")
        binding.inputEt.setText(editTextData)
        viewModel.doRequest(editTextData)
    }*/


    private companion object {
        const val EDIT_TEXT_DATA = "EDIT_TEXT_DATA"
        const val LAST_RESPONSE_DATA = "LAST_RESPONSE_DATA"

    }
}
