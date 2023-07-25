package org.mozilla.social.core.designsystem.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import org.mozilla.social.core.designsystem.icon.MoSoIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoSoAppBar(
    modifier: Modifier = Modifier,
    onMenuClicked: () -> Unit,
    onFeedSelectionClicked: () -> Unit,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        modifier = modifier.testTag("moSoAppBar"),
        title = {
            Text(
                text = "Mozilla Social",
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClicked) {
                Icon(
                    imageVector = MoSoIcons.Menu,
                    contentDescription = "navigation menu",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
        actions = {

            IconButton(onClick = onFeedSelectionClicked) {
                Icon(
                    imageVector = MoSoIcons.Feed,
                    contentDescription = "feed selection",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }

        },
        colors = colors,
        scrollBehavior = scrollBehavior,
    )
}