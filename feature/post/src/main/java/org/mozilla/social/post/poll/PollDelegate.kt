package org.mozilla.social.post.poll

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.analytics.NewPostAnalytics

class PollDelegate(
    private val analytics: NewPostAnalytics,
) : PollInteractions {

    private val _uiState = MutableStateFlow<PollUiState?>(null)
    val uiState = _uiState.asStateFlow()

    override fun onNewPollClicked() {
        analytics.newPollClicked()
        if (uiState.value == null) {
            _uiState.value = newPoll()
        } else {
            _uiState.value = null
        }
    }

    override fun onPollOptionTextChanged(
        optionIndex: Int,
        text: String,
    ) {
        if (text.length > MAX_POLL_CHOICE_CHARACTERS) return
        _uiState.edit {
            this?.copy(
                options =
                    options.toMutableList().apply {
                        removeAt(optionIndex)
                        add(optionIndex, text)
                    },
            )
        }
    }

    override fun onPollOptionDeleteClicked(optionIndex: Int) {
        _uiState.edit {
            this?.copy(
                options =
                    options.toMutableList().apply {
                        removeAt(optionIndex)
                    },
            )
        }
    }

    override fun onAddPollOptionClicked() {
        _uiState.edit {
            this?.copy(
                options =
                    options.toMutableList().apply {
                        add("")
                    },
            )
        }
    }

    override fun onPollDurationSelected(pollDuration: PollDuration) {
        _uiState.edit {
            this?.copy(
                pollDuration = pollDuration,
            )
        }
    }

    override fun onPollStyleSelected(style: PollStyle) {
        _uiState.edit {
            this?.copy(
                style = style,
            )
        }
    }

    override fun onHideCountUntilEndClicked() {
        _uiState.edit {
            this?.copy(
                hideTotals = !hideTotals,
            )
        }
    }

    private fun newPoll(): PollUiState =
        PollUiState(
            options =
                listOf(
                    "",
                    "",
                ),
            pollDuration = PollDuration.ONE_DAY,
            style = PollStyle.SINGLE_CHOICE,
            hideTotals = false,
        )

    companion object {
        const val MAX_POLL_CHOICE_CHARACTERS = 50
    }
}
