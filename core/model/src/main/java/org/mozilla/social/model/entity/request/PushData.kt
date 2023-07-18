package org.mozilla.social.model.entity.request

import org.mozilla.social.model.entity.Alerts

/**
 * Object used to set notification alert settings.
 */
data class PushData(
    val alerts: Alerts
)
