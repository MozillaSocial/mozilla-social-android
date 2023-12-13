package org.mozilla.social.feature.settings.contentpreferences.mutedusers

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

class MutedUsersSettingsViewModel(
    repository: MutesRepository,
    private val analytics: Analytics,
    private val muteAccount: MuteAccount,
    private val remoteMediator: MutesListRemoteMediator,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), SettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()

    @OptIn(ExperimentalPagingApi::class)
    val mutes: Flow<PagingData<MutedUserState>> = repository.getMutesPager(remoteMediator)
        .map { pagingData -> pagingData.map { mutedUser -> mutedUser.toMutedUserState() } }

    suspend fun onMuteButtonClicked(accountId: String) {
        muteAccount(accountId)
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
    MutedUserState(isMuted = isMuted, account = account.toQuickViewUiState())