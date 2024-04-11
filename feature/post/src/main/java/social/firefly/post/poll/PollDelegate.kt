package social.firefly.post.poll

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import social.firefly.common.utils.edit
import social.firefly.core.analytics.NewPostAnalytics
import social.firefly.core.model.Poll
import social.firefly.core.repository.mastodon.StatusRepository

class PollDelegate(
    private val analytics: NewPostAnalytics,
    coroutineScope: CoroutineScope,
    private val statusRepository: StatusRepository,
    private val editStatusId: String?,
) : PollInteractions {

    private val _uiState = MutableStateFlow<PollUiState?>(null)
    val uiState = _uiState.asStateFlow()

    init {
        coroutineScope.launch { editStatusId?.let { populateEditStatus(it) } }
    }

    private suspend fun populateEditStatus(editStatusId: String) {
        statusRepository.getStatusLocal(editStatusId)?.let { status ->
            status.poll?.let {  poll ->
                _uiState.update {
                    PollUiState(
                        options = poll.options.map { it.title },
                        style = if (poll.allowsMultipleChoices) {
                            PollStyle.MULTIPLE_CHOICE
                        } else {
                            PollStyle.SINGLE_CHOICE
                        },
                        pollDuration = poll.getPollDuration(),
                        hideTotals = poll.options.first().votesCount == null
                    )
                }
            }
        }
    }

    private fun Poll.getPollDuration(): PollDuration {
        if (expiresAt == null) return PollDuration.ONE_DAY
        val timeDifference = (expiresAt!!.epochSeconds - Clock.System.now().epochSeconds)
        PollDuration.entries.forEach {
            if (it.inSeconds >= timeDifference) {
                return it
            }
        }
        return PollDuration.ONE_WEEK
    }

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
