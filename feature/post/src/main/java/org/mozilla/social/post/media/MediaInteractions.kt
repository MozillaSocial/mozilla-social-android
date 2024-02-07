package org.mozilla.social.post.media

import android.net.Uri
import org.mozilla.social.common.utils.FileType
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
