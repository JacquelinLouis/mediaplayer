package com.jac.mediaplayer.mediabrowser

import android.content.ComponentName
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jac.mediaplayer.R

/**
 * Recycler view, display available [android.media.browse.MediaBrowser] from their [ComponentName]
 * as a list.
 */
class MediaBrowserAdapter(private val dataSet: List<ComponentName>,
                          private val onClickListener: (ComponentName) -> Unit):
    RecyclerView.Adapter<MediaBrowserAdapter.ViewHolder>() {

    /** View holder, display each [ComponentName] as a row. */
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView =
            view.findViewById(R.id.media_browser_fragment_media_browser_recycler_view_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.media_browser_recycler_view_row_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dataSet[position].toString()
        holder.textView.setOnClickListener{ onClickListener(dataSet[position]) }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}