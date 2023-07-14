package org.mozilla.social.feature.auth

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import okhttp3.HttpUrl

class AuthViewModel : ViewModel() {

    fun onLoginClicked(context: Context) {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(
                context,
                Uri.parse(
                    HttpUrl.Builder()
                        .scheme("https")
                        .host(
                            "mozilla.social"
                        )
                        .addPathSegment("oauth")
                        .addPathSegment("authorize")
                        .addQueryParameter("grand_type", "client_credentials")
                        .addQueryParameter("redirect_uri", AUTH_SCHEME)
                        .addQueryParameter("client_id", "RVeRygd4vQehOI_pXoe375omuuv2NoJUdCt5zJiYAEw")
                        .addQueryParameter("client_secret", "MWNBHd7aGlTug2KfV1wEp90AP4tXc2RhZacudFUNmf8")
                        .build()
                        .toString()
                )
            )
    }

    fun onAuthTokenReceived(token: String) {
        //TODO call this method from main activity onNewIntent and save token somewhere and navigate
    }

    companion object {
        const val AUTH_SCHEME = "mozsoc://auth"
    }
}