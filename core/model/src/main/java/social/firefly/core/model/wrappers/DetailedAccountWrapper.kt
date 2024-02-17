package social.firefly.core.model.wrappers

import social.firefly.core.model.Account
import social.firefly.core.model.Relationship

data class DetailedAccountWrapper(
    val account: Account,
    val relationship: Relationship,
)
