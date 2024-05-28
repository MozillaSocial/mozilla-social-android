package social.firefly.core.model

data class BlockedUser(
    val relationship: Relationship,
    val account: Account,
)
