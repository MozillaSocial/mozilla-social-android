package org.mozilla.social.feature.settings.contentpreferences.mutedusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.MutesRepository

class MutedUsersSettingsViewModel(
    private val repository: MutesRepository,
    private val accountRepository: AccountRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    val mutes: Flow<List<Account>> =
        flow<List<Account>> {
            emit(repository.getMutes())
        }

    // TODO@DA hook up
    fun onMuteButtonClicked(accountId: String) {
        viewModelScope.launch(ioDispatcher) { accountRepository.muteAccount(accountId) }
    }
}
