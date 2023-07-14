package org.mozilla.social

import androidx.lifecycle.ViewModel
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.net.URL

class MainActivityViewModel : ViewModel() {

    fun onTokenReceived(authUri: String) {
        val httpUrl: HttpUrl? = URL(
            authUri.replace("mozsoc://", "http://")
        ).toHttpUrlOrNull()
        val accessToken = httpUrl?.queryParameter("access_token")

    }
}