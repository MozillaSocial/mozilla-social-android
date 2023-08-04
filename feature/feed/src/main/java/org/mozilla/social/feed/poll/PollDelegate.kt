package org.mozilla.social.feed.poll

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.ui.poll.PollInteractions
import org.mozilla.social.core.ui.poll.PollUiState

class PollDelegate(
    private val statusRepository: StatusRepository,
) : PollInteractions {

    lateinit var coroutineScope: CoroutineScope

    override fun onOptionClicked(
        pollUiState: PollUiState,
        optionIndex: Int,
    ) {
//        coroutineScope.launch {
//            try {
//                statusRepository.voteOnPoll(
//                    pollId,
//                    optionIndex
//                )
//            } catch (e: Exception) {
//
//            }
//        }
    }
}