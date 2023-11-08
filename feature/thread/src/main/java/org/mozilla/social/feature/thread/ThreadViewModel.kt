package org.mozilla.social.feature.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.mozilla.social.core.domain.GetThreadUseCase
import org.mozilla.social.core.domain.LoggedInUserAccountId
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.PostCardUiState
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import timber.log.Timber

class ThreadViewModel(
    getThreadUseCase: GetThreadUseCase,
    mainStatusId: String,
    loggedInUserAccountId: LoggedInUserAccountId,
) : ViewModel() {

    var statuses: Flow<List<PostCardUiState>> =
        getThreadUseCase.invoke(mainStatusId).map { statuses ->
            statuses.map { it.toPostCardUiState(loggedInUserAccountId()) }
        }.catch {
            Timber.e(it)
        }

    val postCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java
    ) { parametersOf(viewModelScope) }
}