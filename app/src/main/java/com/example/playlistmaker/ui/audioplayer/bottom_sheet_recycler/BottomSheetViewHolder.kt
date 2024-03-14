package com.example.playlistmaker.ui.audioplayer.bottom_sheet_recycler

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetPlaylistViewBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository
import com.example.playlistmaker.ui.util.Declension
import java.io.File

class BottomSheetViewHolder(
    private val binding: BottomSheetPlaylistViewBinding,
    private val playlistRepository: PlaylistRepository,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist, onClick: (Playlist) -> Unit) = with(binding) {
        tvPlaylistName.text = model.name
        tvTracksCount.text =
            "${model.tracksCount} ${
                Declension.incline(
                    model.tracksCount,
                    "трек",
                    "трека",
                    "треков"
                )
            }"
        Glide.with(itemView)
            .load(File(playlistRepository.getImageFromPrivateStorage(model.name).toUri().path))
            .placeholder(R.drawable.track_placeholder).error(R.drawable.track_placeholder)
            .centerCrop().into(ivPlaylist)

        itemView.setOnClickListener {
            onClick.invoke(model)
        }
    }

}