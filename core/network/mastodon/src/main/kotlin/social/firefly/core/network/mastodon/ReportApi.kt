package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.request.NetworkReportCreate

interface ReportApi {
    suspend fun report(
        body: NetworkReportCreate,
    )
}
