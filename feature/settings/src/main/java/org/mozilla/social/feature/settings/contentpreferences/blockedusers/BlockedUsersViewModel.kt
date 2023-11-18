package org.mozilla.social.feature.settings.contentpreferences.blockedusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.BlocksRepository
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState

class BlockedUsersViewModel(
    private val repository: BlocksRepository,
    private val accountRepository: AccountRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    val blocks: Flow<List<Block>> =
        flow {
            emit(repository.getBlocks().map { it.toBlock() })
        }

    // TODO@DA error handling and unblock
    fun onBlockButtonClicked(accountId: String) {
        viewModelScope.launch(ioDispatcher) { accountRepository.muteAccount(accountId) }
    }
}

private fun Account.toBlock(): Block {
    return Block(toQuickViewUiState())
}
