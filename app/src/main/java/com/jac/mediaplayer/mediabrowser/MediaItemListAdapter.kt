package com.jac.mediaplayer.mediabrowser

import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jac.mediaplayer.R

/**
 * Recycler view, display [MediaBrowserCompat] as a list.
 * @param dataSet the data set to list.
 * @param onClickListener the listener raised on item click.
 */
class MediaItemListAdapter(private val dataSet: List<MediaBrowserCompat.MediaItem>,
                           private val onClickListener: (MediaBrowserCompat.MediaItem) -> Unit):
    RecyclerView.Adapter<MediaItemListAdapter.ViewHolder>() {

    /** View holder, display each [android.support.v4.media.MediaBrowserCompat] as a row. */
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView =
            view.findViewById(R.id.media_browser_fragment_media_item_list_recycler_view_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.media_item_list_recycler_view_row_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dataSet[position].description.title
        holder.textView.setOnClickListener{ onClickListener(dataSet[position]) }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}