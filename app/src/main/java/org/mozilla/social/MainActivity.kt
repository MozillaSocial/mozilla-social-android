package org.mozilla.social

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import okhttp3.HttpUrl
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.feature.auth.AuthViewModel

class MainActivity : ComponentActivity() {

//    val viewModel: MainActivityViewModel = getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MozillaSocialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainActivityScreen(::onLoginClicked)
                }
            }
        }
    }

    private fun onLoginClicked() {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(
                this,
                Uri.parse(
                    HttpUrl.Builder()
                        .scheme("https")
                        .host(
                            "mozilla.social"
                        )
                        .addPathSegment("oauth")
                        .addPathSegment("authorize")
                        .addQueryParameter("grand_type", "client_credentials")
                        .addQueryParameter("redirect_uri", AuthViewModel.AUTH_SCHEME)
                        .addQueryParameter("client_id", "RVeRygd4vQehOI_pXoe375omuuv2NoJUdCt5zJiYAEw")
                        .addQueryParameter("client_secret", "MWNBHd7aGlTug2KfV1wEp90AP4tXc2RhZacudFUNmf8")
                        .build()
                        .toString()
                )
            )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.data.toString().startsWith(AuthViewModel.AUTH_SCHEME)) {
//            viewModel.onTokenReceived(intent?.data.toString())
//            val viewModel: AuthViewModel = koinViewModel()
            //TODO save token somewhere and navigate
        }
    }
}