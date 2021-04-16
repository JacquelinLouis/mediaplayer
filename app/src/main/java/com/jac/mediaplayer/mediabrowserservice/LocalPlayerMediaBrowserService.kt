package com.jac.mediaplayer.mediabrowserservice

import android.media.MediaDescription
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.provider.MediaStore
import android.service.media.MediaBrowserService

class LocalPlayerMediaBrowserService: MediaBrowserService() {

    companion object {
        private val ROOT_ID = LocalPlayerMediaBrowserService::getPackageName.toString() + "ROOT_ID"
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        return BrowserRoot(ROOT_ID, null)
    }

    private fun loadRoot(): MutableList<MediaBrowser.MediaItem> {
        val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME)
        val selection = ""
        val selectionArgs = arrayOf("")
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        val rootChildren = mutableListOf<MediaBrowser.MediaItem>()

        applicationContext.contentResolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
        ).use {
            it?:return rootChildren

            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)

            while(it.moveToNext()) {
                rootChildren.add(MediaBrowser.MediaItem(
                        MediaDescription.Builder()
                                .setMediaId(it.getLong(idColumn).toString())
                                .setTitle(it.getString(nameColumn))
                                .build(),
                        MediaBrowser.MediaItem.FLAG_PLAYABLE
                ))
            }
        }
        return rootChildren
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowser.MediaItem>>) {
        when(parentId) {
            ROOT_ID -> result.sendResult(loadRoot())
            else -> result.sendResult(mutableListOf())
        }
    }
}