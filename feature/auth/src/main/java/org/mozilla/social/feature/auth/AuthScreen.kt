package org.mozilla.social.feature.auth

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import okhttp3.HttpUrl
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme

@Composable
internal fun AuthRoute(
    viewModel: AuthViewModel = koinViewModel(),
) {
    AuthScreen()
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
        onClick = {
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
                            .addQueryParameter("redirect_uri", AuthViewModel.AUTH_SCHEME)
                            .addQueryParameter("client_id", "RVeRygd4vQehOI_pXoe375omuuv2NoJUdCt5zJiYAEw")
                            .addQueryParameter("client_secret", "MWNBHd7aGlTug2KfV1wEp90AP4tXc2RhZacudFUNmf8")
                            .build()
                            .toString()
                    )
                )
        },
        content = {
            Text(
                text = "Login",
            )
        },
    )
}

@Preview
@Composable
internal fun AuthScreenPreview() {
    MozillaSocialTheme {
        AuthScreen()
    }
}
