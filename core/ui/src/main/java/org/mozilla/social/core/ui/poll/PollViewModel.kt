package org.mozilla.social.core.ui.poll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.data.repository.StatusRepository

//TODO delete this class and move logic to a better place
class PollViewModel(
    private val statusRepository: StatusRepository,
    initialState: PollUiState,
) : ViewModel(), PollInteractions {

    private val _pollUiState = MutableStateFlow(initialState)
    val pollUiState = _pollUiState.asStateFlow()

    override fun onOptionClicked(optionIndex: Int) {
        val newPollOptions = pollUiState.value.pollOptions.mapIndexed { index, pollOptionUiState ->
            pollOptionUiState.copy(
                selected = if (optionIndex == index) {
                    !pollOptionUiState.selected
                } else if (pollUiState.value.isMultipleChoice) {
                    pollOptionUiState.selected
                } else {
                    false
                }
            )
        }
        _pollUiState.edit { copy(
            pollOptions = newPollOptions,
            voteButtonEnabled = newPollOptions.any { it.selected }
        ) }
    }

    override fun onVoteClicked() {
        _pollUiState.edit { copy(
            voteButtonEnabled = false
        ) }
        viewModelScope.launch {
            try {
                val selectedOptions = mutableListOf<Int>()
                pollUiState.value.pollOptions.forEachIndexed { index, pollOptionUiState ->
                    if (pollOptionUiState.selected) {
                        selectedOptions.add(index)
                    }
                }
                _pollUiState.update {
                    statusRepository.voteOnPoll(
                        pollUiState.value.pollId,
                        selectedOptions,
                    ).toPollUiState()
                }
            } catch (e: Exception) {
                //TODO show error toast
                _pollUiState.edit { copy(
                    voteButtonEnabled = true
                ) }
            }
        }
    }
}