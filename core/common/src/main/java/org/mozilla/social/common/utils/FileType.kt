package org.mozilla.social.common.utils

import android.content.Context
import android.net.Uri

enum class FileType {
    VIDEO,
    IMAGE,
    UNKNOWN
}

fun Uri.getFileType(context: Context) : FileType {
    val mimeType: String? = context.contentResolver.getType(this)
    return when {
        mimeType?.startsWith("image") ?: false -> FileType.IMAGE
        mimeType?.startsWith("video") ?: false -> FileType.VIDEO
        else -> FileType.UNKNOWN
    }
}