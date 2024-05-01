package social.firefly.feature.settings.contentpreferences.mutedusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import social.firefly.common.utils.StringFactory
import social.firefly.core.analytics.SettingsAnalytics
import social.firefly.core.model.MutedUser
import social.firefly.core.navigation.usecases.NavigateToAccount
import social.firefly.core.repository.mastodon.MutesRepository
import social.firefly.core.repository.paging.remotemediators.MutesListRemoteMediator
import social.firefly.core.ui.common.account.quickview.toQuickViewUiState
import social.firefly.core.ui.common.account.toggleablelist.ToggleableAccountListItemState
import social.firefly.core.usecase.mastodon.account.MuteAccount
import social.firefly.core.usecase.mastodon.account.UnmuteAccount
import social.firefly.feature.settings.R
import timber.log.Timber

class MutedUsersSettingsViewModel(
    repository: MutesRepository,
    remoteMediator: MutesListRemoteMediator,
    private val analytics: SettingsAnalytics,
    private val muteAccount: MuteAccount,
    private val unmuteAccount: UnmuteAccount,
    private val navigateToAccount: NavigateToAccount,
) : ViewModel(), MutedUsersInteractions {

    @OptIn(ExperimentalPagingApi::class)
    val mutes: Flow<PagingData<ToggleableAccountListItemState<MutedButtonState>>> =
        repository.getMutesPager(remoteMediator)
            .map { pagingData -> pagingData.map { mutedUser -> mutedUser.toToggleableState() } }

    override fun onButtonClicked(accountId: String, mutedButtonState: MutedButtonState) {
        viewModelScope.launch(Dispatchers.IO) {
            when (mutedButtonState) {
                is MutedButtonState.Muted -> {
                    try {
                        unmuteAccount(accountId)
                        analytics.accountUnmuted()
                    } catch (e: UnmuteAccount.UnmuteFailedException) {
                        Timber.e(e)
                    }
                }

                is MutedButtonState.Unmuted -> {
                    try {
                        muteAccount(accountId)
                        analytics.accountMuted()
                    } catch (e: MuteAccount.MuteFailedException) {
                        Timber.e(e)
                    }
                }
            }
        }
    }

    override fun onAccountClicked(accountId: String) {
        navigateToAccount(accountId)
    }

    override fun onScreenViewed() {
        analytics.mutedUsersSettingsViewed()
    }
}

fun MutedUser.toToggleableState() =
    ToggleableAccountListItemState(
        buttonState = if (isMuted) MutedButtonState.Muted else MutedButtonState.Unmuted(
            confirmationText = StringFactory.resource(
                R.string.are_you_sure_you_want_to_mute, account.acct
            )
        ),
        account = account.toQuickViewUiState()
    )
