package org.mozilla.social.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = koinViewModel(),
    navigateToLoggedInGraph: () -> Unit,
) {
    val isSignedIn = viewModel.isSignedIn.collectAsState(initial = false).value
    val context = LocalContext.current

    if (isSignedIn) {
        navigateToLoggedInGraph()
    }
    LoginScreen(
        onLoginClicked = { domain ->
            viewModel.onLoginClicked(
                context = context,
                domain = domain
            )
        })
}

@Composable
private fun LoginScreen(onLoginClicked: (String) -> Unit) {
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
        var text by remember { mutableStateOf("mozilla.social") }
        TextField(value = text, onValueChange = { text = it })
        Spacer(modifier = Modifier.padding(80.dp))
        LoginButton(onLoginClicked = { onLoginClicked(text) })
    }
}

@Composable
private fun LoginButton(onLoginClicked: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        onClick = onLoginClicked,
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
        LoginScreen(onLoginClicked = {})
    }
}
