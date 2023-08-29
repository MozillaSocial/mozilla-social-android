package org.mozilla.social.feature.thread

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.domain.GetThreadUseCase
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import org.mozilla.social.model.Status

class ThreadViewModel(
    getThreadUseCase: GetThreadUseCase,
    mainStatusId: String,
) : ViewModel() {

    var statuses: Flow<List<Status>> = getThreadUseCase.invoke(mainStatusId).map {
        it.map { it.toPostCardUiState() }
    }

}