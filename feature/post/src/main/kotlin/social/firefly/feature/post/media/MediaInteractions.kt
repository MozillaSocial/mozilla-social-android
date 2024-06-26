package social.firefly.feature.post.media

import android.net.Uri
import social.firefly.common.utils.FileType
import java.io.File

interface MediaInteractions {
    fun onMediaDescriptionTextUpdated(
        uri: Uri,
        text: String,
    ) = Unit

    fun onDeleteMediaClicked(uri: Uri) = Unit

    fun uploadMedia(
        uri: Uri,
        file: File,
        fileType: FileType,
    ) = Unit
}
