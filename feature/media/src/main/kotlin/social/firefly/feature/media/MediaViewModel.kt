package social.firefly.feature.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import social.firefly.common.utils.StringFactory
import social.firefly.core.model.Attachment
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.StatusRepository

class MediaViewModel(
    private val showSnackbar: ShowSnackbar,
    private val statusRepository: StatusRepository,
    statusId: String,
    startIndex: Int,
) : ViewModel(), MediaInteractions {

    private val _attachments = MutableStateFlow<List<Attachment>>(emptyList())
    val attachments = _attachments.asStateFlow()

    private val _index = MutableStateFlow(startIndex)
    val index = _index.asStateFlow()

    init {
        viewModelScope.launch {
            statusRepository.getStatusLocal(statusId)?.let { status ->
                _attachments.update {
                    status.mediaAttachments
                }
            }
        }
    }

    override fun onDownloadClicked(fileName: String) {
        showSnackbar(StringFactory.resource(R.string.downloading_file, fileName), false)
    }
}