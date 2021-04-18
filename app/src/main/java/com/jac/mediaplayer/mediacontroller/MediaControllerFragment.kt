package com.jac.mediaplayer.mediacontroller

import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jac.mediaplayer.MediaPlayerAndroidViewModel
import com.jac.mediaplayer.R

/** Media controller view, display data such as playback state, metadata, etc. */
class MediaControllerFragment: Fragment(R.layout.media_controller_fragment) {

    /** ViewModel instance. */
    private lateinit var mediaPlayerViewModel: MediaPlayerAndroidViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaPlayerViewModel = MediaPlayerAndroidViewModel.getInstance(requireActivity())

        val titleTextView: TextView = view.findViewById(R.id.media_controller_fragment_metadata_title_text_view)
        val skipPreviousButton: ImageButton = view.findViewById(R.id.media_controller_fragment_control_previous_button)
        val playPauseButton: ImageButton = view.findViewById(R.id.media_controller_fragment_control_play_pause_button)
        val skipNextButton: ImageButton = view.findViewById(R.id.media_controller_fragment_control_next_button)

        mediaPlayerViewModel.playbackStateCompat.observe(viewLifecycleOwner) {
            it?.run {
                when(it.state) {
                    PlaybackStateCompat.STATE_PLAYING -> playPauseButton.setImageResource(R.drawable.outline_pause_foreground)
                    PlaybackStateCompat.STATE_PAUSED -> playPauseButton.setImageResource(R.drawable.outline_play_arrow_foreground)
                }
            }
        }

        mediaPlayerViewModel.metadataCompat.observe(viewLifecycleOwner) {
            it?.run { titleTextView.text = getText(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE) }
        }

        playPauseButton.setOnClickListener {
            mediaPlayerViewModel.playbackStateCompat.value?.run {
                when (state) {
                    PlaybackStateCompat.STATE_PLAYING -> mediaPlayerViewModel.pause()
                    PlaybackStateCompat.STATE_PAUSED -> mediaPlayerViewModel.play()
                }
            }
        }

        skipNextButton.setOnClickListener {
            mediaPlayerViewModel.next()
        }

        skipPreviousButton.setOnClickListener {
            mediaPlayerViewModel.previous()
        }
    }
}