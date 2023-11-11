package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.core.network.mastodon.ReportApi
import org.mozilla.social.core.repository.mastodon.model.status.toNetworkModel
import org.mozilla.social.core.model.request.ReportCreate

class ReportRepository(private val reportApi: ReportApi) {

    suspend fun report(body: ReportCreate) = reportApi.report(body.toNetworkModel())
}