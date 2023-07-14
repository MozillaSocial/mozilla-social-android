package org.mozilla.social.feature.auth

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme

@Composable
fun AuthRoute(
    viewModel: AuthViewModel = koinViewModel()
) {
    AuthScreen(
        onLoginClicked = viewModel::onLoginClicked,
    )
}

@Composable
internal fun AuthScreen(
    onLoginClicked: (Context) -> Unit = {},
) {
    LoginButton(onLoginClicked)
}

@Composable
private fun LoginButton(
    onLoginClicked: (Context) -> Unit,
) {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { onLoginClicked(context) },
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
