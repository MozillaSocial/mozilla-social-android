package org.mozilla.social.core.designsystem.component

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MoSoNavigationDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerContent = drawerContent,
        drawerState = drawerState,
        content = content,
        gesturesEnabled = drawerState.isOpen
    )

}

@Composable
fun MoSoNavigationDrawerItem(text: String, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(text = text) },
        selected = false,
        onClick = onClick,
    )
}