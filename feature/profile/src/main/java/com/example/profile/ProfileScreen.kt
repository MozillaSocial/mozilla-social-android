@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme

@Composable
internal fun ProfileRoute(
    viewModel: ProfileViewModel = koinViewModel(),
) {
    ProfileScreen()
}

@Composable
internal fun ProfileScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    MozillaSocialTheme {
    }
}