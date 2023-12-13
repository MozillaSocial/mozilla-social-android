package org.mozilla.social.feature.settings.contentpreferences.mutedusers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.contentpreferences.blockedusers.BlockedUsersSettingsScreen
import org.mozilla.social.feature.settings.ui.SettingsColumn

@Composable
fun MutedUsersSettingsScreen(
    viewModel: MutedUsersSettingsViewModel = koinViewModel(),
) {

    BlockedUsersSettingsScreen(blocksPagingData = viewModel.mutes, onUnblockClicked = {})
//    MoSoSurface {
//        SettingsColumn(title = stringResource(id = R.string.muted_users_title)) {
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        viewModel.onScreenViewed()
//    }
}
