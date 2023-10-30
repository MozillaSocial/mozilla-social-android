package org.mozilla.social.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
internal fun LoginScreen(
    viewModel: AuthViewModel = koinViewModel(),
) {
    val defaultUrl = viewModel.defaultUrl.collectAsState().value
    val context = LocalContext.current

    LoginScreen(
        defaultUrl = defaultUrl,
        onLoginClicked = { domain ->
            viewModel.onLoginClicked(
                context = context,
                domain = domain
            )
        })
}

@Composable
private fun LoginScreen(defaultUrl: String, onLoginClicked: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(80.dp))
        Text(
            text = stringResource(id = R.string.title_text),
            fontSize = 30.sp
        )
        var text by remember { mutableStateOf(defaultUrl) }
        TextField(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            value = text, singleLine = true, onValueChange = { text = it })
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
                text = stringResource(id = R.string.login_button),
            )
        },
    )
}

@Preview
@Composable
internal fun AuthScreenPreview() {
    MoSoTheme {
        LoginScreen(defaultUrl = "example.com", onLoginClicked = {})
    }
}
