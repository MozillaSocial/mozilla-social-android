package social.firefly.feature.settings.contentpreferences.blockedusers

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
import social.firefly.core.model.BlockedUser
import social.firefly.core.navigation.usecases.NavigateToAccount
import social.firefly.core.repository.mastodon.BlocksRepository
import social.firefly.core.repository.paging.BlocksListRemoteMediator
import social.firefly.core.ui.common.account.quickview.toQuickViewUiState
import social.firefly.core.ui.common.account.toggleablelist.ToggleableAccountListItemState
import social.firefly.core.usecase.mastodon.account.BlockAccount
import social.firefly.core.usecase.mastodon.account.UnblockAccount
import social.firefly.feature.settings.R
import timber.log.Timber

class BlockedUsersViewModel(
    repository: BlocksRepository,
    remoteMediator: BlocksListRemoteMediator,
    private val analytics: SettingsAnalytics,
    private val blockAccount: BlockAccount,
    private val unblockAccount: UnblockAccount,
    private val navigateToAccount: NavigateToAccount,
) : ViewModel(), BlockedUsersInteractions {

    @OptIn(ExperimentalPagingApi::class)
    val blocks: Flow<PagingData<ToggleableAccountListItemState<BlockedButtonState>>> =
        repository.getBlocksPager(remoteMediator = remoteMediator)
            .map { pagingData -> pagingData.map { blockedUser -> blockedUser.toToggleableState() } }

    override fun onButtonClicked(accountId: String, buttonState: BlockedButtonState) {
        viewModelScope.launch(Dispatchers.IO) {
            when (buttonState) {
                is BlockedButtonState.Blocked -> {
                    try {
                        unblockAccount(accountId)
                        analytics.accountUnblocked()
                    } catch (e: UnblockAccount.UnblockFailedException) {
                        Timber.e(e)
                    }
                }

                is BlockedButtonState.Unblocked -> {
                    try {
                        blockAccount(accountId)
                        analytics.accountBlocked()
                    } catch (e: BlockAccount.BlockFailedException) {
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
        analytics.blockedUsersSettingsViewed()
    }
}

fun BlockedUser.toToggleableState() =
    ToggleableAccountListItemState(
        buttonState = if (isBlocked) BlockedButtonState.Blocked else BlockedButtonState.Unblocked(
            confirmationText = StringFactory.resource(
                R.string.are_you_sure_you_want_to_block, account.acct
            )

        ),
        account = account.toQuickViewUiState(),
    )
