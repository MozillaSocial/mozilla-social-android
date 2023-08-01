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

fun Uri.getFileType() : FileType {
    val extension = toString().substring(toString().lastIndexOf('.') + 1)
    return when (extension) {
        "png", "webp", "jpg", "jpeg", "gif", "gifv" -> FileType.IMAGE
        "mp4", "webm" -> FileType.VIDEO
        else -> FileType.UNKNOWN
    }
}