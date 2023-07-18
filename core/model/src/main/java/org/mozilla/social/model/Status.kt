package org.mozilla.social.model

import java.time.Instant

/**
 * Represents a status posted by an account.
 */
public data class Status(

    val statusId: String,

    /**
     * URI of the status used for federation.
     */
    val uri: String,

    /**
     * The account that authored this status.
     */
    val account: Account,

    /**
     * HTML-encoded status content.
     */
    val content: String,


    )
