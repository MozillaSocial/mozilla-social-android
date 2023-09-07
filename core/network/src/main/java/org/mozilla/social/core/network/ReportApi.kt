package org.mozilla.social.core.network

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ReportApi {

    @FormUrlEncoded
    @POST("/api/v1/reports")
    suspend fun report(
        @Field("account_id") accountId: String,
        @Field("status_ids") statusIds: List<String>?,
        @Field("comment") comment: String?,
        @Field("forward") forward: Boolean?,
        @Field("category") category: String?,
        @Field("rule_ids") ruleViolations: List<Int>?,
    )
}