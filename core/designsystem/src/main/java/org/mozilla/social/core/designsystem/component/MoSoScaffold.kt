package org.mozilla.social.core.designsystem.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.mozilla.social.core.designsystem.icon.MosSoBottomSheet

/**
 * Wrapps scaffold in a navigation drawer
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoSoScaffold(
    modifier: Modifier = Modifier,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    navigationDrawerContent: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    navigationDrawerState: DrawerState,
    bottomSheetVisible: Boolean,
    bottomSheetContent: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    MoSoNavigationDrawer(
        drawerState = navigationDrawerState,
        drawerContent = navigationDrawerContent,
        content = {
            Scaffold(
                modifier = modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
                snackbarHost = snackbarHost,
                floatingActionButton = { floatingActionButton() },
                bottomBar = bottomBar,
                topBar = topBar,
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    content(it)
                    MosSoBottomSheet(visible = bottomSheetVisible, content = bottomSheetContent)
                }
            }
        }
    )
}

