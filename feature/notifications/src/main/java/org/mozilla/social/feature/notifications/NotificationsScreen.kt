package org.mozilla.social.feature.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.MoSoTab
import org.mozilla.social.core.ui.common.MoSoTabRow
import org.mozilla.social.core.ui.common.appbar.MoSoTopBar
import org.mozilla.social.core.ui.common.text.MediumTextLabel

@Composable
internal fun NotificationsScreen(
    viewModel: NotificationsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    NotificationsScreen(
        uiState = uiState,
        notificationsInteractions = viewModel,
    )
}

@Composable
private fun NotificationsScreen(
    uiState: NotificationsUiState,
    notificationsInteractions: NotificationsInteractions,
) {
    MoSoSurface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column {
            MoSoTopBar(
                title = stringResource(id = R.string.notifications_title),
                icon = null,
                onIconClicked = {},
                showDivider = false,
            )
            Tabs(
                uiState = uiState,
                notificationsInteractions = notificationsInteractions
            )
        }
    }
}

@Composable
private fun Tabs(
    uiState: NotificationsUiState,
    notificationsInteractions: NotificationsInteractions,
) {
    val context = LocalContext.current

    MoSoTabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
        NotificationsTab.entries.forEach { tabType ->
            MoSoTab(
                modifier =
                Modifier
                    .height(40.dp),
                selected = uiState.selectedTab == tabType,
                onClick = { notificationsInteractions.onTabClicked(tabType) },
                content = {
                    MediumTextLabel(text = tabType.tabTitle.build(context))
                },
            )
        }
    }
}