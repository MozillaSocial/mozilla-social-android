package org.mozilla.social.core.model.paging

import org.mozilla.social.common.MastodonPagingLink
import org.mozilla.social.core.model.Status

data class StatusPagingWrapper(
    val statuses: List<Status>,
    val pagingLinks: List<MastodonPagingLink>?,
)