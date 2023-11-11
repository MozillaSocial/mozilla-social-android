package org.mozilla.social.model.paging

import org.mozilla.social.common.MastodonPagingLink
import org.mozilla.social.model.Status

data class StatusPagingWrapper(
    val statuses: List<Status>,
    val pagingLinks: List<MastodonPagingLink>?,
)