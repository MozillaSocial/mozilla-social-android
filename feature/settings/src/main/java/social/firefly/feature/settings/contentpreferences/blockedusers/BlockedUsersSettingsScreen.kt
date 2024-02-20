package social.firefly.feature.settings.contentpreferences.blockedusers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.navigation.navigationModule
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.account.toggleablelist.ToggleableAccountList
import social.firefly.core.ui.common.account.toggleablelist.ToggleableAccountListItemState
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.feature.settings.R
import social.firefly.feature.settings.ui.SettingsColumn

@Composable
internal fun BlockedUsersSettingsScreen(
    viewModel: BlockedUsersViewModel = koinViewModel()
) {
    BlockedUsersSettingsScreen(
        pagingData = viewModel.blocks,
        blockedUsersInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun BlockedUsersSettingsScreen(
    pagingData: Flow<PagingData<ToggleableAccountListItemState<BlockedButtonState>>>,
    blockedUsersInteractions: BlockedUsersInteractions,
) {
    FfSurface {
        SettingsColumn(title = stringResource(id = R.string.blocked_users_title)) {
            ToggleableAccountList(
                pagingData = pagingData,
                onAccountClicked = blockedUsersInteractions::onAccountClicked,
                onButtonClicked = blockedUsersInteractions::onButtonClicked,
            )
        }
    }
}

@Preview
@Composable
private fun BlockedUsersSettingsScreenPreview() {
    PreviewTheme(
        modules = listOf(navigationModule)
    ) {
        BlockedUsersSettingsScreen(
            pagingData = emptyFlow(),
            blockedUsersInteractions = object : BlockedUsersInteractions {
                override fun onScreenViewed() = Unit
                override fun onButtonClicked(accountId: String, buttonState: BlockedButtonState) =
                    Unit

                override fun onAccountClicked(accountId: String) = Unit
            }
        )
    }
}
