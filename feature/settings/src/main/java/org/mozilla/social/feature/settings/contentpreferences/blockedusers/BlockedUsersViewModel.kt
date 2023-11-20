package org.mozilla.social.feature.settings.contentpreferences.blockedusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.repository.mastodon.BlocksRepository
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState
import org.mozilla.social.core.usecase.mastodon.account.BlockAccount
import org.mozilla.social.core.usecase.mastodon.account.UnblockAccount
import org.mozilla.social.core.usecase.mastodon.remotemediators.BlocksListPagingSource

class BlockedUsersViewModel(
    private val unblockAccount: UnblockAccount,
    private val blocksRepository: BlocksRepository,
) : ViewModel() {

    val pager = Pager(
        config = PagingConfig(20, 40),
    ) {
        BlocksListPagingSource(blocksRepository)
    }.flow.cachedIn(viewModelScope)

    fun onBlockButtonClicked(accountId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            unblockAccount(accountId)
            // TODO@DA how to get paging source to re-fetch the list?
        }
    }
}
