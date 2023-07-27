package org.mozilla.social.post.media

import android.net.Uri
import java.io.File

interface MediaInteractions {
    fun onMediaDescriptionTextUpdated(uri: Uri, text: String) = Unit
    fun onDeleteMediaClicked(uri: Uri) = Unit
    fun onMediaInserted(uri: Uri, file: File) = Unit
}