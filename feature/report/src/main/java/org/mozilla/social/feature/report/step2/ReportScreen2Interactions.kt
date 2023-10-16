package org.mozilla.social.feature.report.step2

interface ReportScreen2Interactions {
    fun onCloseClicked() = Unit
    fun onReportClicked() = Unit
    fun onStatusClicked(statusId: String) = Unit
    fun onRetryClicked() = Unit
}