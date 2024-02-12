package org.mozilla.social.feature.settings.contentpreferences.mutedusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.model.MutedUser
import org.mozilla.social.core.navigation.usecases.NavigateToAccount
import org.mozilla.social.core.repository.mastodon.MutesRepository
import org.mozilla.social.core.repository.paging.MutesListRemoteMediator
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState
import org.mozilla.social.core.ui.common.account.toggleablelist.ToggleableAccountListItemState
import org.mozilla.social.core.usecase.mastodon.account.MuteAccount
import org.mozilla.social.core.usecase.mastodon.account.UnmuteAccount
import org.mozilla.social.feature.settings.R
import org.mozilla.social.core.analytics.SettingsAnalytics
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
