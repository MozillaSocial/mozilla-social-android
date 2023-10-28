package org.mozilla.social.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.mozilla.social.R
import org.mozilla.social.core.designsystem.component.MoSoBottomNavigationBar
import org.mozilla.social.core.designsystem.component.MoSoFloatingActionButton
import org.mozilla.social.core.designsystem.component.MoSoScaffold
import org.mozilla.social.core.designsystem.component.MoSoSnackbar
import org.mozilla.social.core.designsystem.component.MoSoSnackbarHost
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.domain.NavigationEvent
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.navigation.BottomBarTabs
import org.mozilla.social.navigation.MainNavHost
import org.mozilla.social.ui.navigationdrawer.NavigationDrawer

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainActivityScreen(context: Context, navigationEvents: SharedFlow<NavigationEvent>) {

    val appState = rememberAppState()
    LaunchedEffect(key1 = Unit) {
        navigationEvents.onEach {
            when (it) {
                NavigationEvent.NavigateToLogin -> appState.navigateToLoginScreen()
            }
        }.launchIn(this)
    }

    MainNavHost(
        appState = appState,
        context = context,
    )


//    Scaffold(
//        modifier = Modifier,
//        snackbarHost = {
//            MoSoSnackbarHost(appState.snackbarHostState) { snackbarData, snackbarType ->
//                MoSoSnackbar(snackbarData = snackbarData, snackbarType = snackbarType)
//            }
//        },
//        floatingActionButton = {
//            when (currentDestination) {
//                NavigationDestination.Feed -> {
//                    MoSoFloatingActionButton(onClick = appState::navigateToNewPost) {
//                        Icon(
//                            MoSoIcons.plus(),
//                            stringResource(id = R.string.feed_fab_content_description)
//                        )
//                    }
//                }
//
//                else -> {}
//            }
//        },
//        bottomBar = {
//            BottomBar(
//                currentDestination = currentDestination,
//                navigateToTopLevelDestination = appState::navigateToTopLevelDestination
//            )
//        },
//        navigationDrawerState = appState.navigationDrawerState,
//        navigationDrawerContent = {
//            NavigationDrawer(
//                onSettingsClicked = appState::navigateToSettings,
//                onLoggedOut = appState::navigateToLoginScreen,
//            )
//        },
//        bottomSheetContent = { BottomSheetContent() },
//        bottomSheetVisible = appState.bottomSheetVisible.value,
//        content = {
//
//        },
//    )
}



@Composable
private fun BottomSheetContent() {
    Text(text = "feed options coming")
}