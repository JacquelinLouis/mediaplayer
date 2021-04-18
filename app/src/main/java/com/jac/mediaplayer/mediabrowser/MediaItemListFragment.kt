package com.jac.mediaplayer.mediabrowser

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jac.mediaplayer.MediaPlayerAndroidViewModel
import com.jac.mediaplayer.R
import com.jac.mediaplayer.mediacontroller.MediaControllerFragment

class MediaItemListFragment: Fragment(R.layout.media_item_list_fragment) {

    /** ViewModel instance. */
    private lateinit var mediaPlayerViewModel: MediaPlayerAndroidViewModel

    private fun getMediaItemListRecyclerViewAdapter(
            mediaItemList: List<MediaBrowserCompat.MediaItem>?):MediaItemListAdapter? {
        return mediaItemList?.let {
            MediaItemListAdapter(it) { mediaItem ->
                if (mediaItem.isBrowsable) {
                    mediaItem.mediaId?.let { mediaId ->
                        mediaPlayerViewModel.browse(mediaId)
                    }
                } else if (mediaItem.isPlayable) {
                    mediaItem.mediaId?.let { mediaId ->
                        mediaPlayerViewModel.play(mediaId)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaPlayerViewModel = MediaPlayerAndroidViewModel.getInstance(requireActivity())

        val recyclerView = view.findViewById<RecyclerView>(
                R.id.media_browser_fragment_media_item_list_recycler_view)

        mediaPlayerViewModel.mediaItemList.observe(viewLifecycleOwner) { mediaItemList -> run {
                recyclerView.run {
                    adapter = getMediaItemListRecyclerViewAdapter(mediaItemList)
                    layoutManager = LinearLayoutManager(view.context)
                }
            }
        }

        mediaPlayerViewModel.playbackStateCompat.observe(viewLifecycleOwner) {
            it?.let {
                if (it.state == PlaybackStateCompat.STATE_PLAYING) {
                    parentFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<MediaControllerFragment>(R.id.activity_main_fragment_container_view)
                    }
                }
            }
        }
    }
}