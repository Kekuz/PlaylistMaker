package com.example.playlistmaker.ui.playlists.recycler

import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistCoverRepository
import com.example.playlistmaker.ui.util.Convert
import java.io.File

class PlaylistViewHolder(
    private val binding: PlaylistViewBinding,
    private val playlistCoverRepository: PlaylistCoverRepository,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist /*onClick: (Track) -> Unit*/) = with(binding) {
        tvPlaylistName.text = model.name
        //TODO сделать правильные склонения имен
        tvTracksCount.text = "${model.tracksCount} треков"
        Glide.with(itemView)
            .load(File(playlistCoverRepository.getImageFromPrivateStorage(model.name).toUri().path))
            .placeholder(R.drawable.track_placeholder)
            .centerCrop()
            .into(picture)

        /*itemView.setOnClickListener {
            onClick.invoke(model)
        }*/
    }

}