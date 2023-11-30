package org.mozilla.social.feature.settings.contentpreferences.blockedusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.BlocksRepository
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.feature.settings.SettingsInteractions

class BlockedUsersViewModel(
    private val repository: BlocksRepository,
    private val accountRepository: AccountRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val analytics: Analytics,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), SettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()

    val blocks: Flow<List<Block>> =
        flow {
            emit(repository.getBlocks().map { it.toBlock() })
        }

    // TODO@DA error handling and unblock
    fun onBlockButtonClicked(accountId: String) {
        viewModelScope.launch(ioDispatcher) { accountRepository.muteAccount(accountId) }
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.BLOCKED_USERS_SCREEN_IMPRESSION,
            mastodonAccountId = userAccountId,
        )
    }
}

private fun Account.toBlock(): Block {
    return Block(toQuickViewUiState())
}
