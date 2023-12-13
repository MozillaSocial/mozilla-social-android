package org.mozilla.social.feature.settings.contentpreferences.mutedusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.model.MutedUser
import org.mozilla.social.core.repository.mastodon.MutesRepository
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickViewUiState
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.account.MuteAccount
import org.mozilla.social.core.usecase.mastodon.remotemediators.MutesListRemoteMediator
import org.mozilla.social.feature.settings.SettingsInteractions
import org.mozilla.social.feature.settings.contentpreferences.blockedusers.BlockedUserState

class MutedUsersSettingsViewModel(
    repository: MutesRepository,
    private val analytics: Analytics,
    private val muteAccount: MuteAccount,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), SettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()

    private val remoteMediator: MutesListRemoteMediator by KoinJavaComponent.inject(
        MutesListRemoteMediator::class.java,
    )

    @OptIn(ExperimentalPagingApi::class)
    val mutes: Flow<PagingData<BlockedUserState>> = repository.getMutesPager(remoteMediator)
        .map { pagingData -> pagingData.map { mutedUser -> mutedUser.toMutedUserState() } }

    // TODO@DA hook up
    fun onMuteButtonClicked(accountId: String) {
        viewModelScope.launch(Dispatchers.IO) { muteAccount(accountId) }
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.MUTED_USERS_SCREEN_IMPRESSION,
            mastodonAccountId = userAccountId,
        )
    }
}

data class MutedUserState(val isMuted: Boolean, val account: AccountQuickViewUiState)

fun MutedUser.toMutedUserState() =
    BlockedUserState(isMuted, account = account.toQuickViewUiState())