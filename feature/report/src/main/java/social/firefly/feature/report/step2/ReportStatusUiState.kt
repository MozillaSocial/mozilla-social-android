package social.firefly.feature.report.step2

import social.firefly.common.utils.StringFactory
import social.firefly.common.utils.timeSinceNow
import social.firefly.core.model.Status

data class ReportStatusUiState(
    val statusId: String,
    val userName: String,
    val handle: String,
    val avatarUrl: String,
    val htmlStatusText: String,
    val postTimeSince: StringFactory,
    val checked: Boolean,
)

fun Status.toReportStatusUiState(): ReportStatusUiState =
    ReportStatusUiState(
        statusId = statusId,
        userName = account.displayName,
        handle = account.acct,
        avatarUrl = account.avatarStaticUrl,
        htmlStatusText = content,
        postTimeSince = createdAt.timeSinceNow(),
        checked = false,
    )
