package org.mozilla.social.feature.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.domain.AccountIdFlow
import org.mozilla.social.core.domain.GetThreadUseCase
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.PostCardUiState
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import timber.log.Timber

class ThreadViewModel(
    private val analytics: Analytics,
    accountIdFlow: AccountIdFlow,
    getThreadUseCase: GetThreadUseCase,
    mainStatusId: String,
) : ViewModel(), ThreadInteractions {

    private val currentUserAccountId: StateFlow<String> =
        accountIdFlow().stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ""
        )

    var statuses: Flow<List<PostCardUiState>> =
        getThreadUseCase.invoke(mainStatusId).map { statuses ->
            statuses.map { it.toPostCardUiState(currentUserAccountId.value) }
        }.catch {
            Timber.e(it)
        }

    override fun onsScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.THREAD_SCREEN_IMPRESSION,
        )
    }

    val postCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java
    ) { parametersOf(viewModelScope) }
}