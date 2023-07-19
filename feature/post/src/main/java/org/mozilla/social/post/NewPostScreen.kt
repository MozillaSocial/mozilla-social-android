@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package org.mozilla.social.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme

@Composable
internal fun NewPostRoute(
    onStatusPosted: () -> Unit,
    onCloseClicked: () -> Unit,
    viewModel: NewPostViewModel = koinViewModel(parameters = { parametersOf(onStatusPosted) })
) {
    NewPostScreen(
        statusText = viewModel.statusText.collectAsState().value,
        onStatusTextChanged = viewModel::onStatusTextUpdated,
        onPostClicked = viewModel::onPostClicked,
        onCloseClicked = onCloseClicked,
    )
}

@Composable
private fun NewPostScreen(
    statusText: String,
    onStatusTextChanged: (String) -> Unit,
    onPostClicked: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    WindowInsets.Companion.isImeVisible
    Scaffold(
        topBar = { TopBar(
            onPostClicked = onPostClicked,
            onCloseClicked = onCloseClicked,
        ) },
        bottomBar = { BottomBar() },
    ) {
        Box(modifier = Modifier.padding(it)) {
            MainBox(
                statusText,
                onStatusTextChanged,
            )
        }
    }
}

@Composable
private fun MainBox(
    statusText: String,
    onStatusTextChanged: (String) -> Unit,
) {
    TextField(
        modifier = Modifier
            .fillMaxSize(),
        value = statusText,
        onValueChange = onStatusTextChanged,
        label = {
            Text(
                text = "What's happening?"
            )
        }
    )
}

@Composable
private fun TopBar(
    onPostClicked: () -> Unit,
    onCloseClicked: () -> Unit,
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = { onCloseClicked() },
            ) {
                Icon(Icons.Default.Close, "close")
            }
        },
        actions = {
            IconButton(
                onClick = { onPostClicked() },
            ) {
                Icon(Icons.Default.Send, "post")
            }
        }
    )
}

@Composable
private fun BottomBar() {
    BottomAppBar {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Email, "attach image")
        }
    }
}

@Preview
@Composable
private fun NewPostScreenPreview() {
    MozillaSocialTheme {
        NewPostScreen(
            statusText = "",
            onStatusTextChanged = {},
            onPostClicked = {},
            onCloseClicked = {},
        )
    }
}