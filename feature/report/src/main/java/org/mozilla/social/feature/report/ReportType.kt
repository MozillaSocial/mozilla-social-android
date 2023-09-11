package org.mozilla.social.feature.report

enum class ReportType(
    val stringValue: String
) {
    SPAM("spam"),
    VIOLATION("violation"),
    OTHER("other"),
}