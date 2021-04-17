package com.jac.mediaplayer

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.service.media.MediaBrowserService
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.*

/**
 * [AndroidViewModel] implementation to retrieve data from UI.
 * @param app the application to link with.
 */
class MediaPlayerAndroidViewModel(private val app: Application) : AndroidViewModel(app) {

    companion object {

        /**
         * Get an instance of the [MediaPlayerAndroidViewModel].
         * @param viewModelStoreOwner [ViewModelStore] where ViewModels will be stored.
         */
        fun getInstance(viewModelStoreOwner: ViewModelStoreOwner): MediaPlayerAndroidViewModel {
            return ViewModelProvider(viewModelStoreOwner)
                    .get(MediaPlayerAndroidViewModel::class.java)
        }
    }

    /** List of [android.media.browse.MediaBrowser]s' [ComponentName]s. */
    val mediaBrowserComponentNames: List<ComponentName> by lazy { loadMediaBrowserComponentNames() }

    /** Private mutable reference to current [MediaBrowserCompat]. */
    private val mutableMediaBrowserCompat = MutableLiveData<MediaBrowserCompat?>()

    /** Public reference to [MediaBrowserCompat]. */
    val mediaBrowserCompat: LiveData<MediaBrowserCompat?> = mutableMediaBrowserCompat

    /** Private mutable reference to the list of browsable media items. */
    private val mutableMediaItemList = MutableLiveData<List<MediaBrowserCompat.MediaItem>?>()

    /** Public reference to browsable media items. */
    val mediaItemList: LiveData<List<MediaBrowserCompat.MediaItem>?> = mutableMediaItemList

    /** Subscription callback raised on children result available. */
    private val subscriptionCallback = object:MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            mutableMediaItemList.value = children
        }
    }

    /** Connection callback raised on connection success/error to a [MediaBrowserService]. */
    private val connectionCallback = object: MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaBrowserCompat.value?.run { browse(root) }
        }

        override fun onConnectionSuspended() {
            mutableMediaBrowserCompat.value = null
        }

        override fun onConnectionFailed() {
            mutableMediaBrowserCompat.value = null
        }
    }

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

    /**
     * Browse a [android.service.media.MediaBrowserService] based on given [ComponentName].
     * @param componentName the [ComponentName] of the [android.service.media.MediaBrowserService]
     * to browse.
     */
    fun browse(componentName: ComponentName) {
        mutableMediaBrowserCompat.value = MediaBrowserCompat(app.applicationContext,
                componentName,
                connectionCallback,
                null
        ).apply { connect() }
    }

    /**
     * Browse a media item via it identifier.
     * [mediaItemList] will be updated accordingly to the results found.
     * @param id the identifier to browse.
     */
    fun browse(id: String) {
        mediaBrowserCompat.value?.subscribe(id, subscriptionCallback)
    }
}