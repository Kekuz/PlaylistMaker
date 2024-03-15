package com.example.playlistmaker.ui.playlists.recycler

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository
import java.io.File


class PlaylistViewHolder(
    private val binding: PlaylistViewBinding,
    private val playlistRepository: PlaylistRepository,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist/*, onClick: (Track) -> Unit*/) = with(binding) {
        tvPlaylistName.text = model.name
        tvTracksCount.text = itemView.context.resources.getQuantityString(
            R.plurals.plurals_track,
            model.tracksCount,
            model.tracksCount
        )
        Glide.with(itemView)
            .load(File(playlistRepository.getImageFromPrivateStorage(model.name).toUri().path))
            .placeholder(R.drawable.track_placeholder)
            .centerCrop()
            .into(picture)

        /*itemView.setOnClickListener {
            onClick.invoke(model)
        }*/
    }

}