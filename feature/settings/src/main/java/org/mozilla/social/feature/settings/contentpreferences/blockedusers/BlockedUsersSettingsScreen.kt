package org.mozilla.social.feature.settings.contentpreferences.blockedusers

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.account.toggleablelist.ToggleableAccountList
import org.mozilla.social.core.ui.common.account.toggleablelist.ToggleableAccountListItemState
import org.mozilla.social.core.ui.common.account.toggleablelist.ToggleableButtonState
import org.mozilla.social.core.ui.common.button.MoSoButtonPrimaryDefaults
import org.mozilla.social.core.ui.common.button.MoSoButtonSecondaryDefaults
import org.mozilla.social.core.ui.common.button.MoSoButtonTheme
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.ui.SettingsColumn

@Composable
fun BlockedUsersSettingsScreen(viewModel: BlockedUsersViewModel = koinViewModel()) {
    BlockedUsersSettingsScreen(viewModel.blocks, viewModel::onButtonClicked)

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
fun BlockedUsersSettingsScreen(
    pagingData: Flow<PagingData<ToggleableAccountListItemState<BlockedButtonState>>>,
    onButtonClicked: (accountId: String, buttonState: BlockedButtonState) -> Unit,
) {
    MoSoSurface {
        SettingsColumn(title = stringResource(id = R.string.blocked_users_title)) {
            ToggleableAccountList(
                pagingData = pagingData,
                onButtonClicked = onButtonClicked,
            )
        }
    }
}

/**
 * Blocked button state is an implementation of [ToggleableButtonState] which encapsulates the
 * different states a user can be in, and what the text on the button should be for those states
 */
sealed class BlockedButtonState(
    override @get:StringRes val text: Int,
    override val confirmationText: StringFactory? = null,
    override val theme: MoSoButtonTheme,
) : ToggleableButtonState {
    data object Blocked : BlockedButtonState(
        text = R.string.unblock,
        theme = MoSoButtonSecondaryDefaults,
    ) {
        override val confirmationText: StringFactory? = null
    }

    data class Unblocked(override val confirmationText: StringFactory?) : BlockedButtonState(
        text = R.string.block,
        theme = MoSoButtonPrimaryDefaults,
    )
}