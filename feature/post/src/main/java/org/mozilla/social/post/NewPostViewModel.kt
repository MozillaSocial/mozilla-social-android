package org.mozilla.social.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.core.data.repository.StatusRepository

class NewPostViewModel(
    private val statusRepository: StatusRepository,
) : ViewModel() {

    private val _statusText = MutableStateFlow("")
    val statusText: StateFlow<String> = _statusText

    fun onStatusTextUpdated(text: String) {
        _statusText.update { text }
    }

    fun onPostClicked() {
        viewModelScope.launch {
            statusRepository.sendPost(
                statusText = statusText.value
            )
        }
    }
}