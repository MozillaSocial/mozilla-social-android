package org.mozilla.social.core.analytics

import android.content.Context

interface Analytics {
    fun initialize(context: Context) = Unit
    fun uiEngagement(
        engagementType: String? = null,
        engagementValue: String? = null,
        mastodonAccountHandle: String? = null,
        mastodonAccountId: String? = null,
        recommendationId: String? = null,
        uiAdditionalDetail: String? = null,
        uiIdentifier: String? = null
    ) = Unit
    fun uiImpression(
        mastodonAccountHandle: String? = null,
        mastodonAccountId: String? = null,
        mastodonStatusId: String? = null,
        recommendationId: String? = null,
        uiAdditionalDetail: String? = null,
        uiIdentifier: String? = null
    ) = Unit
    fun setAdjustDeviceId(adjustDeviceId: String) = Unit
    fun setFxaAccountId(fxaAccountId: String) = Unit
    fun setMastodonAccountHandle(mastodonAccountHandle: String) = Unit
    fun setMastodonAccountId(mastodonAccountId: String) = Unit
    fun setUserAgent(userAgent: String) = Unit
    fun clearLoggedInIdentifiers() = Unit
}
