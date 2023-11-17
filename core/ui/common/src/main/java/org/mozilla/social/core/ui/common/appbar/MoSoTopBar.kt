package org.mozilla.social.core.ui.common.appbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import org.mozilla.social.core.designsystem.R
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.navigation.usecases.PopNavBackstack
import org.mozilla.social.core.ui.common.divider.MoSoDivider

/**
 * Top app bar which defaults to showing an X button which defaults to popping the navigation
 * backstack
 *
 * @param title
 * @param showCloseButton
 * @param popBackstack
 * @param leftSideContent
 * @param actions
 * @param showDivider
 */
@Composable
fun MoSoCloseableTopAppBar(
    title: String = "",
    showCloseButton: Boolean = true,
    popBackstack: PopNavBackstack = koinInject(),
    actions: @Composable () -> Unit = {},
    showDivider: Boolean = false,
) {
    MoSoTopBar(
        title = title,
        icon = if (showCloseButton) MoSoIcons.backArrow() else null,
        onIconClicked = { popBackstack() },
        actions = actions,
        showDivider = showDivider,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoSoTopBar(
    modifier: Modifier = Modifier,
    title: String = "",
    icon: Painter?,
    onIconClicked: () -> Unit,
    actions: @Composable () -> Unit = {},
    showDivider: Boolean = true,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        MoSoTopBar(
            title = { TopBarTitle(title = title) },
            modifier = modifier,
            navigationIcon = {
                icon?.let {
                    TopBarIconButton(
                        painter = it,
                        onIconClicked = onIconClicked,
                    )
                }
            },
            actions = { actions() },
        )

        if (showDivider) {
            MoSoDivider()
        }
    }
}

@Composable
private fun TopBarTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoSoTopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = MoSoTopBarDefaults.colors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        windowInsets = windowInsets,
        colors = colors,
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun TopBarIconButton(
    painter: Painter,
    onIconClicked: () -> Unit,
) {
    IconButton(
        onClick = { onIconClicked() },
    ) {
        Icon(
            painter = painter,
            contentDescription = stringResource(id = R.string.top_bar_close_content_description),
            tint = MoSoTheme.colors.iconPrimary,
        )
    }
}

object MoSoTopBarDefaults {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun colors(): TopAppBarColors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = MoSoTheme.colors.layer1,
            scrolledContainerColor = MoSoTheme.colors.layer1,
            navigationIconContentColor = MoSoTheme.colors.iconPrimary,
            titleContentColor = MoSoTheme.colors.textPrimary,
            actionIconContentColor = MoSoTheme.colors.iconPrimary,
        )
}

@Preview
@Composable
private fun MoSoTopBarPreview() {
    MoSoTheme {
        MoSoCloseableTopAppBar(
            title = "test",
            actions = {
                Text(text = "rightSide")
            },
        )
    }
}
