package org.mozilla.social.core.ui

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoSoAppBar(
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.inverseSurface),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {

    CenterAlignedTopAppBar(
        modifier = modifier.testTag("moSoAppBar"),
        title = {
            Text(
                text = "Mozilla Social",
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
        },
        colors = colors,
        scrollBehavior = scrollBehavior,
    )

}
