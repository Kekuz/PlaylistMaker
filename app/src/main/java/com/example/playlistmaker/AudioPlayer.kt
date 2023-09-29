package com.example.playlistmaker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.search.Track
import com.example.playlistmaker.search.TrackViewHolder
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding

    companion object {
        private const val TRACK_ICON_CORNER_RADIUS = 30
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBackArrowBtn.setOnClickListener {
            finish()
        }

        val json = intent.extras?.getString("track")
        val track: Track = Gson().fromJson(json, Track::class.java)

        Log.e("trackInfo", track.toString())
        binding.nameTv.text = track.trackName
        binding.authorTv.text = track.artistName
        binding.trackTimeValueTv.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        //В задании написано: "Показывать название альбома (collectionName) (если есть)"
        //iTunes при отсутствии албома возвращает название трека + " - Single", соответсвенно такую строку мы убираем
        if (track.collectionName.endsWith(" - Single")) {
            binding.albumGroup.isVisible = false
        } else {
            binding.albumValueTv.text = track.collectionName
        }
        binding.yearValueTv.text = track.releaseDate.substringBefore('-')
        binding.genreValueTv.text = track.primaryGenreName
        binding.countryValueTv.text = track.country

        val artworkUrl512 =
            track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.big_trackplaceholder)
            .centerCrop()
            .transform(RoundedCorners(TRACK_ICON_CORNER_RADIUS))
            .into(binding.artworkUrl100)
    }
}