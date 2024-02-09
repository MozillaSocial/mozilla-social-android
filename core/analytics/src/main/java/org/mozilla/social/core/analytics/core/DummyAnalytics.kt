package org.mozilla.social.core.analytics.core

import android.content.Context
import timber.log.Timber

internal class DummyAnalytics : Analytics {
    private val tag: String = DummyAnalytics::class.simpleName ?: ""

    override fun initialize(context: Context) {
        Timber.tag(tag).d("initialize")
    }

    override fun uiEngagement(
        engagementType: EngagementType?,
        engagementValue: String?,
        recommendationId: String?,
        uiAdditionalDetail: String?,
        uiIdentifier: String?,
    ) {
        Timber.tag(tag).d("==========")
        Timber.tag(tag).d("engagementType: ${engagementType?.value}")
        Timber.tag(tag).d("engagementValue: $engagementValue")
        Timber.tag(tag).d("recommendationId: $recommendationId")
        Timber.tag(tag).d("uiAdditionalDetail: $uiAdditionalDetail")
        Timber.tag(tag).d("uiIdentifier: $uiIdentifier")
    }

    override fun uiImpression(
        recommendationId: String?,
        uiAdditionalDetail: String?,
        uiIdentifier: String?,
    ) {
        Timber.tag(tag).d("==========")
        Timber.tag(tag).d("recommendationId: $recommendationId")
        Timber.tag(tag).d("uiAdditionalDetail: $uiAdditionalDetail")
        Timber.tag(tag).d("uiIdentifier: $uiIdentifier")
    }

    override fun setAdjustDeviceId(adjustDeviceId: String) {
        Timber.tag(tag).d("adjustDeviceId: $adjustDeviceId")
    }

    override fun setFxaAccountId(fxaAccountId: String) {
        Timber.tag(tag).d("fxaAccountId: $fxaAccountId")
    }

    override fun setMastodonAccountId(mastodonAccountId: String) {
        Timber.tag(tag).d("mastodonAccountId: $mastodonAccountId")
    }

    override fun setUserAgent(userAgent: String) {
        Timber.tag(tag).d("userAgent: $userAgent")
    }

    override fun clearLoggedInIdentifiers() {
        Timber.tag(tag).d("clear")
    }

    override fun appOpened() {
        Timber.tag(tag).d("app opened")
    }

    override fun appBackgrounded() {
        Timber.tag(tag).d("app backgrounded")
    }
}
