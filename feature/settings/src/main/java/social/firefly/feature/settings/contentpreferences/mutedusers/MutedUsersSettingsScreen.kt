package social.firefly.feature.settings.contentpreferences.mutedusers

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
internal fun MutedUsersSettingsScreen(
    viewModel: MutedUsersSettingsViewModel = koinViewModel()
) {
    MutedUsersSettingsScreen(
        pagingData = viewModel.mutes,
        mutedUsersInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun MutedUsersSettingsScreen(
    pagingData: Flow<PagingData<ToggleableAccountListItemState<MutedButtonState>>>,
    mutedUsersInteractions: MutedUsersInteractions,
) {
    FfSurface {
        SettingsColumn(title = stringResource(id = R.string.muted_users_title)) {
            ToggleableAccountList(
                pagingData = pagingData,
                onAccountClicked = mutedUsersInteractions::onAccountClicked,
                onButtonClicked = mutedUsersInteractions::onButtonClicked
            )
        }
    }
}

@Preview
@Composable
private fun MutedUsersSettingsScreenPreview() {
    PreviewTheme(
        modules = listOf(navigationModule)
    ) {
        MutedUsersSettingsScreen(
            pagingData = emptyFlow(),
            mutedUsersInteractions = object : MutedUsersInteractions {
                override fun onScreenViewed() = Unit
                override fun onButtonClicked(
                    accountId: String,
                    mutedButtonState: MutedButtonState
                ) = Unit

                override fun onAccountClicked(accountId: String) = Unit
            }
        )
    }
}
