package com.jac.mediaplayer.mediabrowser

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jac.mediaplayer.MediaPlayerAndroidViewModel
import com.jac.mediaplayer.R

/** Fragment managing UI for [android.media.browse.MediaBrowser] available on the system. */
class MediaBrowserFragment: Fragment(R.layout.media_browser_fragment) {

    /** ViewModel instance. */
    private lateinit var mediaPlayerViewModel: MediaPlayerAndroidViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaPlayerViewModel = MediaPlayerAndroidViewModel(requireActivity().application)

        val recyclerViewAdapter = MediaBrowserAdapter(mediaPlayerViewModel.mediaBrowserComponentNames)
        view.findViewById<RecyclerView>(R.id.media_browser_fragment_media_browser_recycler_view)
            .run {
                adapter = recyclerViewAdapter
                layoutManager = LinearLayoutManager(view.context)
            }
    }

}