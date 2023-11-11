package org.mozilla.social.model.request

import org.mozilla.social.model.Alerts

/**
 * Object used to set notification alert settings.
 */
data class PushData(
    val alerts: Alerts
)
