package org.mozilla.social.core.repository.mastodon.exceptions

class GetAccountFailedException(
    val errorCode: Int,
    override val message: String,
    override val cause: Throwable?,
) : Exception(
    message,
    cause,
)