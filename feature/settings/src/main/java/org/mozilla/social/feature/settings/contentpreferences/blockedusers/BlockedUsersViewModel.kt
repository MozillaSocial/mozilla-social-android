package org.mozilla.social.feature.settings.contentpreferences.blockedusers

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
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.model.BlockedUser
import org.mozilla.social.core.repository.mastodon.BlocksRepository
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState
import org.mozilla.social.core.ui.common.account.toggleablelist.ToggleableAccountListItemState
import org.mozilla.social.core.usecase.mastodon.account.BlockAccount
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.account.UnblockAccount
import org.mozilla.social.core.usecase.mastodon.remotemediators.BlocksListRemoteMediator
import org.mozilla.social.feature.settings.R
import org.mozilla.social.feature.settings.SettingsInteractions

class BlockedUsersViewModel(
    repository: BlocksRepository,
    remoteMediator: BlocksListRemoteMediator,
    private val analytics: Analytics,
    private val blockAccount: BlockAccount,
    private val unblockAccount: UnblockAccount,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), SettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()

    @OptIn(ExperimentalPagingApi::class)
    val blocks: Flow<PagingData<ToggleableAccountListItemState<BlockedButtonState>>> =
        repository.getBlocksPager(remoteMediator = remoteMediator)
            .map { pagingData -> pagingData.map { blockedUser -> blockedUser.toToggleableState() } }

    fun onButtonClicked(accountId: String, buttonState: BlockedButtonState) {
        viewModelScope.launch(Dispatchers.IO) {
            when (buttonState) {
                is BlockedButtonState.Blocked -> unblockAccount(accountId)
                is BlockedButtonState.Unblocked -> blockAccount(accountId)
            }
        }
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.BLOCKED_USERS_SCREEN_IMPRESSION,
            mastodonAccountId = userAccountId,
        )
    }
}

fun BlockedUser.toToggleableState() =
    ToggleableAccountListItemState(
        buttonState = if (isBlocked) BlockedButtonState.Blocked(
            confirmationText = StringFactory.resource(
                R.string.are_you_sure_you_want_to_unblock, account.acct
            )
        ) else BlockedButtonState.Unblocked(
            confirmationText = StringFactory.resource(
                R.string.are_you_sure_you_want_to_block, account.acct
            )

        ),
        account = account.toQuickViewUiState(),
    )