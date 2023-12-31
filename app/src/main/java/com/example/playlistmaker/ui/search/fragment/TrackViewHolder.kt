package com.example.playlistmaker.ui.search.fragment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track


class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object{
        private const val TRACK_ICON_CORNER_RADIUS = 10
    }

    private val trackIcon: ImageView = itemView.findViewById(R.id.track_iv)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val trackAuthor: TextView = itemView.findViewById(R.id.track_author)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)


    fun bind(model: Track, onClick: (Track) -> Unit) {
        trackName.text = model.trackName
        trackAuthor.text = model.artistName
        trackTime.text = model.trackTime
        Glide.with(itemView).load(model.artworkUrl100).placeholder(R.drawable.track_placeholder)
            .centerCrop()
            .transform(RoundedCorners(TRACK_ICON_CORNER_RADIUS))
            .into(trackIcon)

        itemView.setOnClickListener{
            onClick.invoke(model)
        }
    }

}
