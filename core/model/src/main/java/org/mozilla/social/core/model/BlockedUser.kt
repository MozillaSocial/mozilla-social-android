package org.mozilla.social.core.model

data class BlockedUser(val isBlocked: Boolean, val account: Account)
data class MutedUser(val isMuted: Boolean, val account: Account)
