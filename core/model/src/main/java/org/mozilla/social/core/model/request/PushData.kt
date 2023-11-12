package org.mozilla.social.core.model.request

import org.mozilla.social.core.model.Alerts

/**
 * Object used to set notification alert settings.
 */
data class PushData(
    val alerts: Alerts
)
