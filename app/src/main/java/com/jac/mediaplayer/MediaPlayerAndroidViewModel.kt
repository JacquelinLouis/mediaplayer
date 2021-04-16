package com.jac.mediaplayer

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.service.media.MediaBrowserService
import androidx.lifecycle.AndroidViewModel

/**
 * [AndroidViewModel] implementation to retrieve data from UI.
 * @param app the application to link with.
 */
class MediaPlayerAndroidViewModel(private val app: Application) : AndroidViewModel(app) {

    /** List of [android.media.browse.MediaBrowser]s' [ComponentName]s. */
    val mediaBrowserComponentNames: List<ComponentName> by lazy { loadMediaBrowserComponentNames() }

    /**
     * Retrieve and return the list of [android.media.browse.MediaBrowser]'s [ComponentName]s
     * available on the device.
     * @return the list of [android.media.browse.MediaBrowser]s' [ComponentName]s
     */
    private fun loadMediaBrowserComponentNames(): List<ComponentName> {
        val intent = Intent(MediaBrowserService.SERVICE_INTERFACE)
        return app.packageManager.queryIntentServices(intent, 0).mapNotNull {
            it.serviceInfo?.run { ComponentName(packageName, name) }
        }
    }
}