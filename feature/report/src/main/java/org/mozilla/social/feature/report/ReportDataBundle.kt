package org.mozilla.social.feature.report

import kotlinx.serialization.Serializable
import org.mozilla.social.model.InstanceRule

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
    ): ReportDataBundle()

    @Serializable
    data class ReportDataBundleForScreen3(
        val reportAccountId: String,
        val reportAccountHandle: String,
        val didUserReportAccount: Boolean,
    ): ReportDataBundle()
}