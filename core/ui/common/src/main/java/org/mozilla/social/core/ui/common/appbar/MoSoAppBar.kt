package org.mozilla.social.core.ui.common.appbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoSoAppBar(
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    title: @Composable () -> Unit,
    colors: TopAppBarColors =
        TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MoSoTheme.colors.layer1,
            navigationIconContentColor = MoSoTheme.colors.iconPrimary,
        ),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        modifier = modifier.testTag("moSoAppBar"),
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
        scrollBehavior = scrollBehavior,
    )
}
