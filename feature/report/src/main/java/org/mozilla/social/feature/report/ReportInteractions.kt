package org.mozilla.social.feature.report

import org.mozilla.social.model.InstanceRule

interface ReportInteractions {
    fun onCloseClicked() = Unit
    fun onReportClicked() = Unit
    fun onReportTypeSelected(reportType: ReportType) = Unit
    fun onServerRuleClicked(rule: InstanceRule) = Unit
    fun onAdditionCommentTextChanged(text: String) = Unit
    fun onSendToExternalServerClicked() = Unit
}