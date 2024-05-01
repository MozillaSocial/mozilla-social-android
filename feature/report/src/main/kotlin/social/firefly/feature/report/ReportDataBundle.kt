package social.firefly.feature.report

import kotlinx.serialization.Serializable
import social.firefly.core.model.InstanceRule

sealed class ReportDataBundle {
    @Serializable
    data class ReportDataBundleForScreen2(
        val reportAccountId: String,
        val reportAccountHandle: String,
        val reportStatusId: String?,
        val reportType: ReportType,
        val checkedInstanceRules: List<InstanceRule>,
        val additionalText: String,
        val sendToExternalServer: Boolean,
    ) : ReportDataBundle()

    @Serializable
    data class ReportDataBundleForScreen3(
        val reportAccountId: String,
        val reportAccountHandle: String,
        val didUserReportAccount: Boolean,
    ) : ReportDataBundle()
}
