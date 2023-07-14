package org.mozilla.social.feature.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme

@Composable
fun AuthRoute(
    onLoginClicked: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    AuthScreen(
        onLoginClicked = onLoginClicked,
    )
}

@Composable
internal fun AuthScreen(
    onLoginClicked: () -> Unit = {},
) {
    LoginButton(onLoginClicked)
}

@Composable
private fun LoginButton(
    onLoginClicked: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { onLoginClicked() },
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
