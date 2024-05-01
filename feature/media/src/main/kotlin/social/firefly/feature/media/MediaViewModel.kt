package social.firefly.feature.media

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import social.firefly.common.utils.StringFactory
import social.firefly.core.navigation.usecases.ShowSnackbar

class MediaViewModel(
    private val showSnackbar: ShowSnackbar,
    startIndex: Int,
) : ViewModel(), MediaInteractions {

    private val _index = MutableStateFlow(startIndex)
    val index = _index.asStateFlow()

    override fun onDownloadClicked(fileName: String) {
        showSnackbar(StringFactory.resource(R.string.downloading_file, fileName), false)
    }
}