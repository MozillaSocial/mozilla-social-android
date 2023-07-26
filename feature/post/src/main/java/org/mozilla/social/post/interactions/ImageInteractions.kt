package org.mozilla.social.post.interactions

import android.net.Uri
import java.io.File

interface ImageInteractions {
    fun onImageDescriptionTextUpdated(uri: Uri, text: String) = Unit
    fun onDeleteImageClicked(uri: Uri) = Unit
    fun onImageInserted(uri: Uri, file: File) = Unit
}