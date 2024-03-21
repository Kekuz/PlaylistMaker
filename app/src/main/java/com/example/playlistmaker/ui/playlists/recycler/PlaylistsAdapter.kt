package com.example.playlistmaker.ui.playlists.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistViewBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlist.api.interactor.PlaylistCoverInteractor

class PlaylistsAdapter(
    private val playlistCoverInteractor: PlaylistCoverInteractor,
    private val onClick: (Playlist) -> Unit,
) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {

    private val playlists = mutableListOf<Playlist>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaylistViewBinding.inflate(inflater, parent, false)
        return PlaylistsViewHolder(binding, playlistCoverInteractor)
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position], onClick)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun clearPlaylists() = playlists.clear()

    fun addPlaylists(playlists: List<Playlist>) =
        this.playlists.addAll(playlists)

}