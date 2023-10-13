package org.mozilla.social.feature.report

import kotlinx.serialization.Serializable
import org.mozilla.social.model.InstanceRule

@Serializable
data class ReportDataBundle(
    val reportAccountId: String,
    val reportAccountHandle: String,
    val reportStatusId: String?,
    val reportType: ReportType,
    val checkedInstanceRules: List<InstanceRule>,
    val additionalText: String,
    val sendToExternalServer: Boolean,
)