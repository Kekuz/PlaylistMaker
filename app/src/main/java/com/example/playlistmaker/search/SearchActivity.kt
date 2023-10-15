package com.example.playlistmaker.search

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
import com.example.playlistmaker.AudioPlayer
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val searchRunnable = Runnable {
        val response = binding.inputEt.text.toString()
        if(response != "") {
            doResponse(response)
            Log.e("Debounce send response", response)
        }
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
            if (clickDebounce()){
                searchHistory.add(it)
                val intent = Intent(this, AudioPlayer::class.java)
                intent.putExtra("track", Gson().toJson(it))
                startActivity(intent)
                historyAdapter.notifyDataSetChanged()
            }

        }


    private lateinit var historyAdapter: TrackAdapter

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesAPI::class.java)

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
            doResponse(lastResponse)
        }

        binding.trackRv.adapter = trackAdapter

        binding.inputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doResponse(binding.inputEt.text.toString())
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

    private fun doResponse(text: String) {
        if (binding.inputEt.text.isNotEmpty()) {
            binding.searchPb.isVisible = true
            iTunesService.search(text).enqueue(object :
                Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == REQUEST_SUCCEEDED) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            binding.errorIv.isVisible = false
                            binding.errorTv.isVisible = false
                            binding.errorBtn.isVisible = false
                            binding.searchPb.isVisible = false
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
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showErrorPictureAndText(getString(R.string.internet_problems))
                    lastResponse = text
                }

            })
        }
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
        doResponse(editTextData)
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

    private fun clickDebounce() : Boolean {
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
        const val BASE_URL = "https://itunes.apple.com"
        const val HISTORY_SHARED_PREFERENCES = "history_shared_preferences"
        const val REQUEST_SUCCEEDED = 200
        const val ERROR_RESPONSE_NOT_FOUND = 404
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
