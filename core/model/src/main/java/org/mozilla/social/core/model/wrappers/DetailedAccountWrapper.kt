package org.mozilla.social.core.model.wrappers

import org.mozilla.social.core.model.Account
import org.mozilla.social.core.model.Relationship

data class DetailedAccountWrapper(
    val account: Account,
    val relationship: Relationship,
)