package com.example.playlistmaker.ui.playlist.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.playlist.api.repository.PlaylistRepository

class BottomSheetPlaylistAdapter(
    private val onClick: (Track) -> Unit,
    private val onLongClick: (Track) -> Unit
) : RecyclerView.Adapter<BottomSheetPlaylistViewHolder>() {

    private val tracks = mutableListOf<Track>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetPlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TrackViewBinding.inflate(inflater, parent, false)
        return BottomSheetPlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BottomSheetPlaylistViewHolder, position: Int) {
        holder.bind(tracks[position], onClick, onLongClick)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun clearTracks() = tracks.clear()

    fun removeTrack(track: Track): Int {
        var removedIndex = -1
        this.tracks.forEachIndexed { index, it ->
            if (it == track) {
                removedIndex = index
            }
        }
        tracks.remove(track)
        return removedIndex
    }

    fun addTracks(playlists: List<Track>) =
        this.tracks.addAll(playlists)
}