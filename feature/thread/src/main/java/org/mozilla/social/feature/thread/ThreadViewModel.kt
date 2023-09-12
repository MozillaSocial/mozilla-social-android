package org.mozilla.social.feature.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.domain.AccountIdFlow
import org.mozilla.social.core.domain.GetThreadUseCase
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.PostCardUiState
import org.mozilla.social.core.ui.postcard.toPostCardUiState

class ThreadViewModel(
    statusRepository: StatusRepository,
    accountRepository: AccountRepository,
    log: Log,
    accountIdFlow: AccountIdFlow,
    getThreadUseCase: GetThreadUseCase,
    mainStatusId: String,
    onPostClicked: (String) -> Unit,
    onReplyClicked: (String) -> Unit,
    onReportClicked: (accountId: String, statusId: String) -> Unit,
) : ViewModel() {

    private val currentUserAccountId: StateFlow<String> =
        accountIdFlow().stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ""
        )

    var statuses: Flow<List<PostCardUiState>> =
        getThreadUseCase.invoke(mainStatusId).map { statuses ->
            statuses.map { it.toPostCardUiState(currentUserAccountId.value) }
        }

    val postCardDelegate = PostCardDelegate(
        coroutineScope = viewModelScope,
        statusRepository = statusRepository,
        accountRepository = accountRepository,
        log = log,
        onPostClicked = onPostClicked,
        onReplyClicked = onReplyClicked,
        onReportClicked = onReportClicked,
    )

}