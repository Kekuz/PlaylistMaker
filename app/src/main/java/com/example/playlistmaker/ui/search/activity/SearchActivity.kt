package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.player.models.TrackForPlayer
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.models.SearchState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModel<SearchViewModel>()

    private val inputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    }


    private val onClick: (Track) -> Unit =
        {
            if (viewModel.clickDebounce()) {
                if (it.previewUrl != null) {
                    Log.d("Track opened", it.toString())
                    viewModel.addToHistory(it)

                    TrackForPlayer.init(it)// Добавляем трек в синглтон
                    val intent = Intent(this, AudioPlayerActivity::class.java)
                    startActivity(intent)
                    historyAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, getString(R.string.no_music_on_server), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

    private val tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(tracks, onClick)

    private lateinit var history: List<Track>
    private lateinit var historyAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindAdapters()
        bindButtons()
        bindHistoryShowing()
        bindKeyboardSearchButton()

        binding.inputEt.addTextChangedListener(getTextWatcher())

        viewModel.observeState().observe(this) {
            render(it)
        }
    }

    private fun bindAdapters() = with(binding) {
        trackRv.adapter = trackAdapter

        history = viewModel.getHistory()
        historyAdapter = TrackAdapter(history, onClick)
        historyRv.adapter = historyAdapter
    }

    private fun bindButtons() = with(binding) {
        clearHistoryBtn.setOnClickListener {
            showEmpty()
            viewModel.clearHistory()
        }

        errorBtn.setOnClickListener {
            showEmpty()
            viewModel.reloadRequest()
        }

        ivBackArrowBtn.setOnClickListener {
            finish()
        }

        clearIv.setOnClickListener {
            inputEt.setText("")
            showEmpty()
            inputMethodManager?.hideSoftInputFromWindow(
                this@SearchActivity.currentFocus?.windowToken,
                0
            )
            inputEt.clearFocus()
        }
    }

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showClearBnt(s)
                viewModel.searchDebounce(s.toString())
            }
        }
    }

    private fun bindHistoryShowing() = with(binding) {
        inputEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEt.text.isEmpty() && viewModel.getHistory().isNotEmpty()) {
                historySv.isVisible = true
                clearHistoryBtn.isVisible = true
            } else {
                historySv.isVisible = false
                clearHistoryBtn.isVisible = false
            }
        }
    }

    private fun bindKeyboardSearchButton() {
        binding.inputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.atOnceRequest(binding.inputEt.text.toString())

                inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
                true
            }
            false
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


    private fun showContent(content: List<Track>) = with(binding) {
        //Это чтобы при повороте почистить ресайклер, а то че он
        if (inputEt.text.toString() != "") {
            searchPb.isVisible = false
            errorIv.isVisible = false
            errorTv.isVisible = false
            errorBtn.isVisible = false
            tracks.clear()
            tracks.addAll(content)
            trackAdapter.notifyDataSetChanged()
            trackRv.isVisible = true

            historySv.isVisible = false
            clearHistoryBtn.isVisible = false
        }
    }

    private fun showEmptyContent() = with(binding) {
        //Это чтобы при повороте почистить ресайклер, а то че он
        if (inputEt.text.toString() != "") {
            searchPb.isVisible = false
            trackRv.isVisible = false

            errorIv.setBackgroundResource(R.drawable.not_found_icon)
            errorTv.text = getString(R.string.nothing_found)
            errorIv.isVisible = true
            errorTv.isVisible = true

            historySv.isVisible = false
            clearHistoryBtn.isVisible = false
        }
    }

    private fun showError(errorMessage: String) = with(binding) {
        //Это чтобы при повороте почистить ресайклер, а то че он
        if (inputEt.text.toString() != "") {
            searchPb.isVisible = false
            trackRv.isVisible = false

            errorIv.setBackgroundResource(R.drawable.internet_problem_icon)
            errorTv.text = errorMessage
            errorBtn.isVisible = true
            errorIv.isVisible = true
            errorTv.isVisible = true

            historySv.isVisible = false
            clearHistoryBtn.isVisible = false
        }

    }

    private fun showLoading() = with(binding) {
        searchPb.isVisible = true

        errorIv.isVisible = false
        errorBtn.isVisible = false
        errorTv.isVisible = false

        trackRv.isVisible = false
        historySv.isVisible = false
        clearHistoryBtn.isVisible = false
    }

    private fun showClearBnt(s: CharSequence?) {
        binding.clearIv.isVisible = !s.isNullOrEmpty()
    }

    private fun showEmpty() = with(binding) {
        trackRv.isVisible = false

        errorIv.isVisible = false
        errorBtn.isVisible = false
        errorTv.isVisible = false

        searchPb.isVisible = false

        clearHistoryBtn.isVisible = false
        historySv.isVisible = false
    }
}
