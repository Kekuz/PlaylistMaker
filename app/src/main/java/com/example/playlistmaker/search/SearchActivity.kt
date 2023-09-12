package com.example.playlistmaker.search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.playlistmaker.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private companion object {
        const val EDIT_TEXT_DATA = "EDIT_TEXT_DATA"
        const val LAST_RESPONSE_DATA = "LAST_RESPONSE_DATA"
        const val BASE_URL = "https://itunes.apple.com"
    }

    private lateinit var errorIV: ImageView
    private lateinit var errorTV: TextView
    private lateinit var errorBtn: Button
    private lateinit var inputEditText: EditText

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesAPI::class.java)

    private var editTextData = ""
    private var lastResponse: String = ""

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        val backArrowBtn = findViewById<ImageView>(R.id.iv_back_arrow_btn)
        val clearButton = findViewById<ImageView>(R.id.clear_iv)

        errorIV = findViewById(R.id.error_iv)
        errorTV = findViewById(R.id.error_tv)
        errorBtn = findViewById(R.id.error_btn)

        errorBtn.setOnClickListener{
            doResponse(lastResponse)
        }

        inputEditText = findViewById(R.id.input_et)
        inputEditText.setText(editTextData)

        val trackRV = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.track_rv)
        trackRV.adapter = adapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                doResponse(inputEditText.text.toString())
                true
            }
            false
        }



        backArrowBtn.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            tracks.clear()
            adapter.notifyDataSetChanged()
            errorIV.visibility = View.GONE
            errorTV.visibility = View.GONE
            errorBtn.visibility = View.GONE

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

            inputEditText.clearFocus()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                editTextData = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)

    }
    private fun doResponse(text: String){
        if (inputEditText.text.isNotEmpty()) {
            iTunesService.search(text).enqueue(object :
                Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            errorIV.visibility = View.GONE
                            errorTV.visibility = View.GONE
                            errorBtn.visibility =  View.GONE
                            tracks.addAll(response.body()?.results!!)
                            Log.e("MyLog", tracks.toString())
                            adapter.notifyDataSetChanged()
                        } else if (tracks.isEmpty()) {
                            showErrorPictureAndText(getString(R.string.nothing_found))
                        }
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
            errorIV.setBackgroundResource(R.drawable.not_found_icon)
            errorTV.text = getString(R.string.nothing_found)
        } else if (text == getString(R.string.internet_problems)) {
            errorIV.setBackgroundResource(R.drawable.internet_problem_icon)
            errorTV.text = getString(R.string.internet_problems)
            errorBtn.visibility =  View.VISIBLE
        }
        errorIV.visibility = View.VISIBLE
        errorTV.visibility = View.VISIBLE
        tracks.clear()
        adapter.notifyDataSetChanged()
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
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}
