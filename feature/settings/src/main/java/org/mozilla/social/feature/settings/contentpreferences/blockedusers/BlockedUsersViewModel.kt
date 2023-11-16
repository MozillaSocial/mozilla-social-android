package org.mozilla.social.feature.settings.contentpreferences.blockedusers

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.BlocksRepository
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState

class BlockedUsersViewModel(
    private val repository: BlocksRepository,
    private val accountRepository: AccountRepository,
) : ViewModel() {

    val blocks: Flow<List<Block>> = flow {
        emit(repository.getBlocks().map { it.toBlock() })
    }

    // TODO@DA hook up
    suspend fun onBlockButtonClicked(accountId: String) {
        accountRepository.muteAccount(accountId)
    }
}

private fun Account.toBlock(): Block {
    return Block(toQuickViewUiState())
}
