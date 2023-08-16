package org.mozilla.social.core.database.model.wrappers

import org.mozilla.social.core.database.model.DatabaseAccount
import org.mozilla.social.core.database.model.DatabasePoll
import org.mozilla.social.core.database.model.DatabaseStatus

data class StatusWrapper(
    val status: DatabaseStatus,
    val account: DatabaseAccount,
    val poll: DatabasePoll?,
    val boostedStatus: DatabaseStatus?,
    val boostedAccount: DatabaseAccount?,
    val boostedPoll: DatabasePoll?,
)
