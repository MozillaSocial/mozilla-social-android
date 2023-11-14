package org.mozilla.social.feature.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.PostCardUiState
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import org.mozilla.social.core.usecase.mastodon.thread.GetThread
import timber.log.Timber

class ThreadViewModel(
    private val analytics: Analytics,
    getThread: GetThread,
    mainStatusId: String,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), ThreadInteractions {

    var statuses: Flow<List<PostCardUiState>> =
        getThread.invoke(mainStatusId).map { statuses ->
            statuses.map { it.toPostCardUiState(getLoggedInUserAccountId()) }
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