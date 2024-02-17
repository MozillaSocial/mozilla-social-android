package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.request.NetworkReportCreate
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportApi {
    @POST("/api/v1/reports")
    suspend fun report(
        @Body body: NetworkReportCreate,
    )
}
