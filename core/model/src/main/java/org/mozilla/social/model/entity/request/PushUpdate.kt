package org.mozilla.social.model.entity.request

/**
 * Object to update push alert settings.
 */
data class PushUpdate(

    /**
     * Push notification settings.
     */
    val data: PushData
)
