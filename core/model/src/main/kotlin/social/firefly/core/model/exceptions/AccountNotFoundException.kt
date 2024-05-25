package social.firefly.core.model.exceptions

class AccountNotFoundException(
    override val cause: Throwable? = null,
) : Exception(
    cause,
)