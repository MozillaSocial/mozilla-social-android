package social.firefly.core.model.request

/**
 * Object to update push alert settings.
 */
data class PushUpdate(
    /**
     * Push notification settings.
     */
    val data: PushData,
)
