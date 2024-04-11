package social.firefly.core.repository.mastodon.exceptions

class AccountNotFoundException(
    override val cause: Throwable?,
) : Exception(
    cause,
)