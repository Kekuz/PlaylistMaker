package com.example.playlistmaker.ui.audioplayer.bottom_sheet_recycler

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetPlaylistViewBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistCoverInteractor
import java.io.File

class BottomSheetViewHolder(
    private val binding: BottomSheetPlaylistViewBinding,
    private val playlistCoverInteractor: PlaylistCoverInteractor,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist, onClick: (Playlist) -> Unit) = with(binding) {
        tvPlaylistName.text = model.name
        tvTracksCount.text = itemView.context.resources.getQuantityString(
            R.plurals.plurals_track,
            model.tracksCount,
            model.tracksCount
        )

        Glide.with(itemView)
            .load(File(playlistCoverInteractor.getImageFromPrivateStorage(model.pathToCover).toUri().path))
            .placeholder(R.drawable.track_placeholder).error(R.drawable.track_placeholder)
            .centerCrop().into(ivPlaylist)

        itemView.setOnClickListener {
            onClick.invoke(model)
        }
    }

}