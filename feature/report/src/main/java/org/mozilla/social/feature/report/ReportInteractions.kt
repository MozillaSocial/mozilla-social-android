package org.mozilla.social.feature.report

interface ReportInteractions {
    fun onCloseClicked() = Unit
    fun onReportClicked() = Unit
    fun onReportTypeSelected(reportType: ReportType) = Unit
    fun onServerRuleClicked(ruleId: Int) = Unit
}