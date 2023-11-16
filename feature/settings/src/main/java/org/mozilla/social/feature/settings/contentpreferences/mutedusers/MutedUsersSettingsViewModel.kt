package org.mozilla.social.feature.settings.contentpreferences.mutedusers

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.MutesRepository

class MutedUsersSettingsViewModel(
    private val repository: MutesRepository,
    private val accountRepository: AccountRepository,
): ViewModel() {

    val mutes: Flow<List<Account>> = flow<List<Account>> {
        emit(repository.getMutes())
    }

    // TODO@DA hook up
    suspend fun onMuteButtonClicked(accountId: String) {
        accountRepository.muteAccount(accountId)
    }
}

