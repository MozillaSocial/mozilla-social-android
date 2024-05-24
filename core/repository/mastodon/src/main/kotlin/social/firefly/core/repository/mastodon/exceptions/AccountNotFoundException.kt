package social.firefly.core.repository.mastodon.exceptions

class AccountNotFoundException(
    override val cause: Throwable? = null,
) : Exception(
    cause,
)