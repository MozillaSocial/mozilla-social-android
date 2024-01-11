package org.mozilla.social.core.model.paging

import org.mozilla.social.common.MastodonPagingLink
import org.mozilla.social.core.model.Notification

data class NotificationsPagingWrapper(
    val notifications: List<Notification>,
    val pagingLinks: List<MastodonPagingLink>?,
)