package social.firefly.core.ui.common.appbar

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import social.firefly.core.designsystem.R
import social.firefly.core.designsystem.icon.FfIcons
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.navigation.usecases.PopNavBackstack
import social.firefly.core.ui.common.divider.FfDivider

/**
 * Top app bar which defaults to showing an X button which defaults to popping the navigation
 * backstack
 *
 * @param title
 * @param showCloseButton
 * @param popBackstack
 * @param actions
 * @param showDivider
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FfCloseableTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    showCloseButton: Boolean = true,
    popBackstack: PopNavBackstack = koinInject(),
    actions: @Composable () -> Unit = {},
    showDivider: Boolean = false,
    colors: TopAppBarColors = FfTopBarDefaults.colors(),
) {
    FfTopBar(
        modifier = modifier,
        title = title,
        icon = if (showCloseButton) FfIcons.backArrow() else null,
        onIconClicked = { popBackstack() },
        actions = actions,
        showDivider = showDivider,
        colors = colors,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FfTopBar(
    modifier: Modifier = Modifier,
    title: String = "",
    icon: Painter?,
    onIconClicked: () -> Unit,
    actions: @Composable () -> Unit = {},
    showDivider: Boolean = true,
    colors: TopAppBarColors = FfTopBarDefaults.colors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        FfTopBar(
            title = { TopBarTitle(title = title) },
            navigationIcon = {
                icon?.let {
                    TopBarIconButton(
                        painter = it,
                        onIconClicked = onIconClicked,
                    )
                }
            },
            actions = { actions() },
            scrollBehavior = scrollBehavior,
            colors = colors,
        )

        if (showDivider) {
            FfDivider()
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
fun FfTopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = FfTopBarDefaults.colors(),
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
            tint = FfTheme.colors.iconPrimary,
        )
    }
}

object FfTopBarDefaults {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun colors(
        containerColor: Color = FfTheme.colors.layer1,
        scrolledContainerColor: Color = FfTheme.colors.layer1,
        navigationIconContentColor: Color = FfTheme.colors.iconPrimary,
        titleContentColor: Color = FfTheme.colors.textPrimary,
        actionIconContentColor: Color = FfTheme.colors.iconPrimary,
    ): TopAppBarColors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = scrolledContainerColor,
            navigationIconContentColor = navigationIconContentColor,
            titleContentColor = titleContentColor,
            actionIconContentColor = actionIconContentColor,
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun FfTopBarPreview() {
    FfTheme {
        FfCloseableTopAppBar(
            title = "test",
            actions = {
                Text(text = "rightSide")
            },
        )
    }
}
