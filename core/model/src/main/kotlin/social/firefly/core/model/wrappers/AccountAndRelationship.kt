package social.firefly.core.model.wrappers

import social.firefly.core.model.Account
import social.firefly.core.model.Relationship

data class AccountAndRelationship(
    val relationship: Relationship,
    val account: Account,
)
