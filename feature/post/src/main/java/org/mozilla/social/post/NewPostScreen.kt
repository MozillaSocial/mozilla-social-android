@file:OptIn(ExperimentalMaterial3Api::class)

package org.mozilla.social.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme

@Composable
internal fun NewPostRoute(
    viewModel: NewPostViewModel = koinViewModel()
) {
    NewPostScreen(
        statusText = viewModel.statusText.collectAsState().value,
        onStatusTextChanged = viewModel::onStatusTextUpdated,
    )
}

@Composable
private fun NewPostScreen(
    statusText: String,
    onStatusTextChanged: (String) -> Unit,
) {
    Scaffold(
        topBar = { TopBar() },
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
private fun TopBar() {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = {  },
            ) {
                Icon(Icons.Default.Close, "close")
            }
        },
        actions = {
            IconButton(
                onClick = {  },
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
            onStatusTextChanged = {}
        )
    }
}