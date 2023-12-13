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
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.model.BlockedUser
import org.mozilla.social.core.repository.mastodon.BlocksRepository
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState
import org.mozilla.social.core.usecase.mastodon.account.BlockAccount
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.account.UnblockAccount
import org.mozilla.social.core.usecase.mastodon.remotemediators.BlocksListRemoteMediator
import org.mozilla.social.feature.settings.SettingsInteractions

class BlockedUsersViewModel(
    repository: BlocksRepository,
    remoteMediator: BlocksListRemoteMediator,
    private val analytics: Analytics,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), SettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()

    @OptIn(ExperimentalPagingApi::class)
    val blocks: Flow<PagingData<BlockedUserState>> =
        repository.getBlocksPager(remoteMediator = remoteMediator)
            .map { pagingData -> pagingData.map { blockedUser -> blockedUser.toBlockedUserState() } }

    @Suppress("UnusedParameter")
    fun onBlockButtonClicked(accountId: String) {
        // TODO@DA
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.BLOCKED_USERS_SCREEN_IMPRESSION,
            mastodonAccountId = userAccountId,
        )
    }
}

fun BlockedUser.toBlockedUserState() =
    BlockedUserState(isBlocked = isBlocked, account = account.toQuickViewUiState())