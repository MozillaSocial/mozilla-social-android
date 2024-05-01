package social.firefly.core.model.paging

import social.firefly.common.MastodonPagingLink
import social.firefly.core.model.Notification

data class NotificationsPagingWrapper(
    val notifications: List<Notification>,
    val pagingLinks: List<MastodonPagingLink>?,
)