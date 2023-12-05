package org.mozilla.social.feature.settings.contentpreferences.blockedusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.BlocksRepository
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.account.UnblockAccount
import org.mozilla.social.core.usecase.mastodon.remotemediators.BlocksListPagingSource
import org.mozilla.social.feature.settings.SettingsInteractions

class BlockedUsersViewModel(
    private val repository: BlocksRepository,
    private val unblockAccount: UnblockAccount,
    private val accountRepository: AccountRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val analytics: Analytics,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), SettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()

    val pager = Pager(
        config = PagingConfig(20, 40),
    ) {
        BlocksListPagingSource(repository)
    }.flow.map { it.map { it.toQuickViewUiState() } }.cachedIn(viewModelScope)

    fun onBlockButtonClicked(accountId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            unblockAccount(accountId)
            // TODO@DA how to get paging source to re-fetch the list? Invalidate on the paging
            //  source isn't doing anything but that might be because the refresh key fun isn't setup
            //  correctly
        }
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.BLOCKED_USERS_SCREEN_IMPRESSION,
            mastodonAccountId = userAccountId,
        )
    }
}
