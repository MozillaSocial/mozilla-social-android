package org.mozilla.social.model

import kotlinx.datetime.LocalDate

/**
 * Represents a weekly bucket of instance activity.
 */
data class Activity(

    /**
     * First day of the week corresponding to this activity.
     */
    val week: LocalDate,

    /**
     * Number of statuses posted during the week.
     */
    val statusCount: Long,

    /**
     * Number of logins during the week.
     */
    val loginCount: Long,

    /**
     * Number of users that registered during the week.
     */
    val registrationCount: Long
)
