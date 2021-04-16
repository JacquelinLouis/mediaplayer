package com.jac.mediaplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.jac.mediaplayer.mediabrowser.MediaBrowserFragment

/** Main activity started on application launch. */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<MediaBrowserFragment>(R.id.activity_main_fragment_container_view)
        }
    }
}