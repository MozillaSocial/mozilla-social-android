package org.mozilla.social.model.entity.request

/**
 * Object used to submit a new report.
 */
data class ReportCreate(

    /**
     * ID of the account to report.
     */
    val accountId: String,

    /**
     * ID of statuses to attach to the report, for context.
     */
    val statusIds: List<String>?,

    /**
     * Reason for the report.
     *
     * Max 1000 characters by default.
     */
    val comment: String?,

    /**
     * If the account is remote, should the report be forwarded to the remote admin?
     */
    val forwardToRemoteAdmin: Boolean?
)
