package org.mozilla.social.feature.report.step1

import org.mozilla.social.feature.report.ReportType
import org.mozilla.social.core.model.InstanceRule

interface ReportScreen1Interactions {
    fun onCloseClicked() = Unit
    fun onNextClicked() = Unit
    fun onReportTypeSelected(reportType: ReportType) = Unit
    fun onServerRuleClicked(rule: InstanceRule) = Unit
    fun onAdditionCommentTextChanged(text: String) = Unit
    fun onSendToExternalServerClicked() = Unit
    fun onScreenViewed() = Unit
}