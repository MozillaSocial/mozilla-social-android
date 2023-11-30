package org.mozilla.social.feature.settings.contentpreferences.mutedusers

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
import org.mozilla.social.core.repository.mastodon.MutesRepository
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.feature.settings.SettingsInteractions

class MutedUsersSettingsViewModel(
    private val repository: MutesRepository,
    private val accountRepository: AccountRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val analytics: Analytics,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), SettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()

    val mutes: Flow<List<Account>> =
        flow<List<Account>> {
            emit(repository.getMutes())
        }

    // TODO@DA hook up
    fun onMuteButtonClicked(accountId: String) {
        viewModelScope.launch(ioDispatcher) { accountRepository.muteAccount(accountId) }
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.MUTED_USERS_SCREEN_IMPRESSION,
            mastodonAccountId = userAccountId,
        )
    }
}
