package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.core.model.request.ReportCreate
import org.mozilla.social.core.network.mastodon.ReportApi
import org.mozilla.social.core.repository.mastodon.model.status.toNetworkModel

class ReportRepository(private val reportApi: ReportApi) {
    @PreferUseCase
    suspend fun report(body: ReportCreate) = reportApi.report(body.toNetworkModel())
}
