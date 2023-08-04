package org.mozilla.social.core.ui.poll

interface PollInteractions {
    fun onOptionClicked(
        pollUiState: PollUiState,
        optionIndex: Int,
    ) = Unit
}