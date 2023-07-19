package org.mozilla.social.model.entity.request

import java.nio.ByteBuffer

/**
 * A file to be uploaded to a Mastodon API.
 */
data class File(

    /**
     * Raw file contents.
     */
    val contents: ByteBuffer,

    /**
     * Name of the file.
     */
    val filename: String,

    /**
     * MIME content type of the file.
     */
    val contentType: String
)