package com.example.playlistmaker.ui.audioplayer.bottom_sheet_recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.BottomSheetPlaylistViewBinding
import com.example.playlistmaker.databinding.PlaylistViewBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository

class BottomSheetAdapter(
    private val playlistRepository: PlaylistRepository
    /*private val onClick: (Playlist) -> Unit,*/
) : RecyclerView.Adapter<BottomSheetViewHolder>() {

    private val playlists = mutableListOf<Playlist>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BottomSheetPlaylistViewBinding.inflate(inflater, parent, false)
        return BottomSheetViewHolder(binding, playlistRepository)
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(playlists[position] /*onClick*/)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun clearPlaylists() = playlists.clear()

    fun addPlaylists(playlists: List<Playlist>) =
        this.playlists.addAll(playlists)
}