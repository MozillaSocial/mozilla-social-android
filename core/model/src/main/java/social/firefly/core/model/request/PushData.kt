package social.firefly.core.model.request

import social.firefly.core.model.Alerts

/**
 * Object used to set notification alert settings.
 */
data class PushData(
    val alerts: Alerts,
)
