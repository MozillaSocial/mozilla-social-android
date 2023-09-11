package org.mozilla.social.core.data.repository

import org.mozilla.social.core.network.ReportApi

class ReportRepository(
    private val reportApi: ReportApi,
) {

    suspend fun report(
        accountId: String,
        statusIds: List<String>? = null,
        comment: String? = null,
        forward: Boolean? = null,
        category: String? = null,
        ruleViolations: List<Int>? = null,
    ) = reportApi.report(
        accountId = accountId,
        statusIds = statusIds,
        comment = comment,
        forward = forward,
        category = category,
        ruleViolations = ruleViolations
    )
}