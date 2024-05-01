package social.firefly.feature.report.step1

import social.firefly.core.model.InstanceRule
import social.firefly.feature.report.ReportType

interface ReportScreen1Interactions {
    fun onCloseClicked() = Unit

    fun onNextClicked() = Unit

    fun onReportTypeSelected(reportType: ReportType) = Unit

    fun onServerRuleClicked(rule: InstanceRule) = Unit

    fun onAdditionCommentTextChanged(text: String) = Unit

    fun onSendToExternalServerClicked() = Unit

    fun onScreenViewed() = Unit
}
