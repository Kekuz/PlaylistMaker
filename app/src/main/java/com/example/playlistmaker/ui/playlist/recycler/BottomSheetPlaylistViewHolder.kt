package com.example.playlistmaker.ui.playlist.recycler

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetPlaylistViewBinding
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository
import java.io.File

class BottomSheetPlaylistViewHolder(
    private val binding: TrackViewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track, onClick: (Track) -> Unit, onLongClick: (Track) -> Unit) = with(binding) {
        trackName.text = model.trackName
        trackAuthor.text = model.artistName
        trackTime.text = model.trackTime

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.track_placeholder).error(R.drawable.track_placeholder)
            .centerCrop().into(trackIv)

        itemView.setOnClickListener {
            onClick.invoke(model)
        }

        itemView.setOnLongClickListener {
            onLongClick.invoke(model)
            true
        }
    }

}