package org.mozilla.social.core.repository.mastodon

import org.mozilla.social.core.network.mastodon.ReportApi
import org.mozilla.social.core.network.mastodon.model.NetworkReport
import org.mozilla.social.core.network.mastodon.model.request.NetworkReportCreate
import org.mozilla.social.core.repository.mastodon.mappers.toNetworkModel
import org.mozilla.social.model.request.ReportCreate

class ReportRepository(private val reportApi: ReportApi) {

    suspend fun report(body: ReportCreate) = reportApi.report(body.toNetworkModel())
}