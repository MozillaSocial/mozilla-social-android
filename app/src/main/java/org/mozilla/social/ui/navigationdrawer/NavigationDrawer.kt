package org.mozilla.social.ui.navigationdrawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.R
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoModalDrawerSheet
import org.mozilla.social.core.designsystem.component.MoSoNavigationDrawerItem
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
fun NavigationDrawer(
    onSettingsClicked: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: NavigationDrawerViewModel = koinViewModel(
        parameters = {
            parametersOf(
                onLoggedOut,
            )
        }
    )
) {
    MoSoModalDrawerSheet {
        NavigationDrawerContent(
            onSettingsClicked = onSettingsClicked,
            onLogoutClicked = { viewModel.onLogoutClicked() }
        )
    }
}

@Composable
fun ColumnScope.NavigationDrawerContent(
    onSettingsClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
) {
    Column {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(id = R.string.navigation_drawer_title),
        )

        MoSoDivider()
        MoSoNavigationDrawerItem(
            text = stringResource(id = R.string.navigation_drawer_item_settings),
            onClick = onSettingsClicked,
        )
    }

    Spacer(modifier = Modifier.weight(1f))

    LogoutButton(
        modifier = Modifier.padding(bottom = 8.dp),
        onClick = onLogoutClicked,
    )
}

@Composable
private fun LogoutButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    //TODO convert to MosoButton when we make it
    MoSoSurface(
        color = MoSoTheme.colors.actionPrimary,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 8.dp),
            text = stringResource(id = R.string.logout),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MoSoTheme.colors.textActionPrimary
        )
    }
}