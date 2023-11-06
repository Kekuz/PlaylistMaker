package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.interactor.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.audioplayer.AudioPlayerActivity
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val searchRunnable = Runnable {
        val requestText = binding.inputEt.text.toString()
        doRequest(requestText)
        Log.d("Debounce send response", requestText)

    }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private val sharedPrefs by lazy {
        getSharedPreferences(
            HISTORY_SHARED_PREFERENCES,
            MODE_PRIVATE
        )
    }
    private val searchHistory by lazy { SearchHistory(sharedPrefs) }

    private val onClick: (Track) -> Unit =
        {
            if (clickDebounce()) {
                searchHistory.add(it)
                val intent = Intent(this, AudioPlayerActivity::class.java)
                intent.putExtra("track", Gson().toJson(it))
                startActivity(intent)
                historyAdapter.notifyDataSetChanged()
            }

        }


    private lateinit var historyAdapter: TrackAdapter


    private var editTextData = ""
    private var lastResponse: String = ""

    private val tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(tracks, onClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyAdapter = TrackAdapter(searchHistory.tracks, onClick)
        binding.historyRv.adapter = historyAdapter

        binding.clearHistoryBtn.setOnClickListener {
            searchHistory.clear()
            binding.historySv.isVisible = false
        }


        binding.errorBtn.setOnClickListener {
            doRequest(lastResponse)
        }

        binding.trackRv.adapter = trackAdapter

        binding.inputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doRequest(binding.inputEt.text.toString())
                handler.removeCallbacks(searchRunnable)
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
                true
            }
            false
        }

        binding.inputEt.setOnFocusChangeListener { _, hasFocus ->
            binding.historySv.visibility =
                if (hasFocus && binding.inputEt.text.isEmpty() && searchHistory.tracks.isNotEmpty())
                    View.VISIBLE
                else
                    View.GONE
        }

        binding.ivBackArrowBtn.setOnClickListener {
            finish()
        }

        binding.clearIv.setOnClickListener {
            binding.inputEt.setText("")
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            binding.errorIv.isVisible = false
            binding.errorTv.isVisible = false
            binding.errorBtn.isVisible = false

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

            binding.inputEt.clearFocus()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIv.visibility = clearButtonVisibility(s)
                editTextData = s.toString()
                searchDebounce()

                if (binding.inputEt.hasFocus() && s?.isEmpty() == true && searchHistory.tracks.isNotEmpty()) {
                    binding.historySv.isVisible = true

                    binding.errorIv.isVisible = false
                    binding.errorTv.isVisible = false
                    binding.errorBtn.isVisible = false
                    tracks.clear()
                    trackAdapter.notifyDataSetChanged()
                } else
                    binding.historySv.isVisible = false

            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        binding.inputEt.addTextChangedListener(simpleTextWatcher)

    }
    //Как обрабатывать ошибку если делать запрос без интернета я не знаю((((
    private fun doRequest(text: String) {
        if(text.isNotEmpty()){
            binding.searchPb.isVisible = true
            Creator.provideMoviesInteractor().searchTrack(text, object : TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>) {
                    Log.e("Response", foundTracks.toString())
                    runOnUiThread{
                        tracks.clear()
                        if (foundTracks.isNotEmpty()) {
                            binding.errorIv.isVisible = false
                            binding.errorTv.isVisible = false
                            binding.errorBtn.isVisible = false
                            tracks.addAll(foundTracks)
                            trackAdapter.notifyDataSetChanged()
                        } else {
                            showErrorPictureAndText(getString(R.string.nothing_found))
                        }
                        binding.searchPb.isVisible = false
                    }
                }
            })
        }

        /*if (binding.inputEt.text.isNotEmpty()) {
            binding.searchPb.isVisible = true
            iTunesService.search(text).enqueue(object :
                Callback<TrackSearchResponse> {
                override fun onResponse(
                    call: Call<TrackSearchResponse>,
                    response: Response<TrackSearchResponse>
                ) {
                    if (response.code() == REQUEST_SUCCEEDED) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            binding.errorIv.isVisible = false
                            binding.errorTv.isVisible = false
                            binding.errorBtn.isVisible = false
                            tracks.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                        } else if (tracks.isEmpty()) {
                            showErrorPictureAndText(getString(R.string.nothing_found))
                        }
                    } else if (response.code() == ERROR_RESPONSE_NOT_FOUND) {
                        showErrorPictureAndText(getString(R.string.nothing_found))
                    } else {
                        showErrorPictureAndText(getString(R.string.internet_problems))
                        lastResponse = text
                    }
                    binding.searchPb.isVisible = false
                }

                override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                    showErrorPictureAndText(getString(R.string.internet_problems))
                    lastResponse = text
                }

            })
        }*/
    }

    private fun showErrorPictureAndText(text: String) {
        if (text == getString(R.string.nothing_found)) {
            binding.errorIv.setBackgroundResource(R.drawable.not_found_icon)
            binding.errorTv.text = getString(R.string.nothing_found)
        } else if (text == getString(R.string.internet_problems)) {
            binding.errorIv.setBackgroundResource(R.drawable.internet_problem_icon)
            binding.errorTv.text = getString(R.string.internet_problems)
            binding.errorBtn.isVisible = true
        }
        binding.errorIv.isVisible = true
        binding.errorTv.isVisible = true
        tracks.clear()
        trackAdapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_DATA, editTextData)
        outState.putString(LAST_RESPONSE_DATA, lastResponse)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextData = savedInstanceState.getString(EDIT_TEXT_DATA, "")
        lastResponse = savedInstanceState.getString(LAST_RESPONSE_DATA, "")
        binding.inputEt.setText(editTextData)
        doRequest(editTextData)
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private companion object {
        const val EDIT_TEXT_DATA = "EDIT_TEXT_DATA"
        const val LAST_RESPONSE_DATA = "LAST_RESPONSE_DATA"
        const val HISTORY_SHARED_PREFERENCES = "history_shared_preferences"
        //const val REQUEST_SUCCEEDED = 200
        //const val ERROR_RESPONSE_NOT_FOUND = 404
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
