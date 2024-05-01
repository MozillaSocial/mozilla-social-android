package social.firefly.core.repository.mastodon

import social.firefly.common.annotations.PreferUseCase
import social.firefly.core.model.request.ReportCreate
import social.firefly.core.network.mastodon.ReportApi
import social.firefly.core.repository.mastodon.model.status.toNetworkModel

class ReportRepository(private val reportApi: ReportApi) {
    @PreferUseCase
    suspend fun report(body: ReportCreate) = reportApi.report(body.toNetworkModel())
}
