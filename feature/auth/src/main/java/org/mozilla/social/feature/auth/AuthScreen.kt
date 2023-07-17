package org.mozilla.social.feature.auth

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import okhttp3.HttpUrl
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme

@Composable
internal fun AuthRoute(
    viewModel: AuthViewModel = koinViewModel(),
    onAuthenticated: () -> Unit,
) {
    when (viewModel.isSignedIn.collectAsState(initial = null).value) {
        true -> onAuthenticated()
        false -> AuthScreen()
        else -> {
            // Splash screen
        }
    }
}

@Composable
internal fun AuthScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(80.dp))
        Text(
            text = "Mozilla Social",
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.padding(80.dp))
        LoginButton()
    }
}

@Composable
private fun LoginButton() {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .padding(20.dp)
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
