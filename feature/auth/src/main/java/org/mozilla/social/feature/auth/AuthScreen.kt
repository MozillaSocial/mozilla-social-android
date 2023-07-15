package org.mozilla.social.feature.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.util.Consumer
import okhttp3.HttpUrl
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme

@Composable
internal fun AuthRoute(
    onSignedIn: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val activity = LocalContext.current as ComponentActivity
    val log: Log = get()

    when (viewModel.uiState.collectAsState().value) {
        is UiState.SignedIn -> onSignedIn()
        else -> {}
    }

    AuthScreen()
    if (activity.intent?.data.toString().startsWith(AuthViewModel.AUTH_SCHEME)) {
        log.d("intent with data")
        viewModel.onTokenReceived(activity.intent?.data.toString())
    }
//    DisposableEffect(Unit) {
//
//        log.d("intent disposable")
//        val listener = Consumer<Intent> { intent ->
//            log.d("intent consumer")
//            if (intent?.data.toString().startsWith(AuthViewModel.AUTH_SCHEME)) {
//                log.d("intent with data")
//                viewModel.onTokenReceived(intent?.data.toString())
//            }
//        }
//        activity.addOnNewIntentListener(listener)
//        onDispose { activity.removeOnNewIntentListener(listener) }
//    }
}

@Composable
internal fun AuthScreen() {
    LoginButton()
}

@Composable
private fun LoginButton() {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { openCustomTabs(context) },
        content = {
            Text(
                text = "Login",
            )
        },
    )
}

/**
 *  credentials generated with:
 curl -X POST \
 -F 'client_name=Mozilla Social Android' \
 -F 'redirect_uris=mozsoc://auth' \
 -F 'scopes=read write push follow' \
 https://mozilla.social/api/v1/apps
 */
private fun openCustomTabs(context: Context) {
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
                    .addQueryParameter("response_type", "code")
                    .addQueryParameter("grand_type", "client_credentials")
                    .addQueryParameter("redirect_uri", AuthViewModel.AUTH_SCHEME)
                    .addQueryParameter("client_id", "MoJ_c0aOfXE-8RllOBvAKIeHUpr3usA5u3vS5HJEJ0M")
                    .addQueryParameter("client_secret", "1rg6NsrXDuR81UM_ljyv_FNDj8TaInk6pbRok2eqmiM")
                    .build()
                    .toString()
            )
        )
}

@Preview
@Composable
internal fun AuthScreenPreview() {
    MozillaSocialTheme {
        AuthScreen()
    }
}
