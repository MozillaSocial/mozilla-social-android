package org.mozilla.social.core.analytics.glean

import android.content.Context
import mozilla.telemetry.glean.BuildInfo
import mozilla.telemetry.glean.Glean
import org.mozilla.social.core.analytics.GleanMetrics.Identifiers
import org.mozilla.social.core.analytics.GleanMetrics.Ui
import java.util.Calendar

interface Analytics {
    fun initialize(context: Context)

    fun uiEngagement(
        engagementType: String?,
        engagementValue: String?,
        mastodonAccountHandle: String?,
        mastodonAccountId: String?,
        recommendationId: String?,
        uiAdditionalDetail: String?,
        uiIdentifier: String?
    )

    fun uiImpression(
        mastodonAccountHandle: String?,
        mastodonAccountId: String?,
        mastodonStatusId: String?,
        recommendationId: String?,
        uiAdditionalDetail: String?,
        uiIdentifier: String?
    )

    fun setAdjustDeviceId(adjustDeviceId: String)

    fun setFxaAccountId(fxaAccountId: String)

    fun setMastodonAccountHandle(mastodonAccountHandle: String)


    fun setMastodonAccountId(mastodonAccountId: String)

    fun setUserAgent(userAgent: String)

    fun clearLoggedInIdentifiers()
}

class GleanAnalytics : Analytics {
    override fun initialize(context: Context) {
        val buildInfo = BuildInfo("1", "1", Calendar.getInstance())

        Glean.setLogPings(true)
        Glean.setDebugViewTag("moso-android-debug")

        Glean.initialize(
            applicationContext = context,
            uploadEnabled = true,
            buildInfo = buildInfo
        )
    }

    override fun uiEngagement(
        engagementType: String?,
        engagementValue: String?,
        mastodonAccountHandle: String?,
        mastodonAccountId: String?,
        recommendationId: String?,
        uiAdditionalDetail: String?,
        uiIdentifier: String?
    ) {
        Ui.engagement.record(extra = Ui.EngagementExtra(
            engagementType,
            engagementValue,
            mastodonAccountHandle,
            mastodonAccountId,
            recommendationId,
            uiAdditionalDetail,
            uiIdentifier
        ))
    }

    override fun uiImpression(
        mastodonAccountHandle: String?,
        mastodonAccountId: String?,
        mastodonStatusId: String?,
        recommendationId: String?,
        uiAdditionalDetail: String?,
        uiIdentifier: String?
    ) {
        Ui.impression.record(extra = Ui.ImpressionExtra(
            mastodonAccountHandle,
            mastodonAccountId,
            mastodonStatusId,
            recommendationId,
            uiAdditionalDetail,
            uiIdentifier
        ))
    }

    override fun setAdjustDeviceId(adjustDeviceId: String) {
        Identifiers.adjustDeviceId.set(adjustDeviceId)
    }

    override fun setFxaAccountId(fxaAccountId: String) {
        Identifiers.fxaAccountId.set(fxaAccountId)
    }

    override fun setMastodonAccountHandle(mastodonAccountHandle: String) {
        Identifiers.mastodonAccountHandle.set(mastodonAccountHandle)
    }

    override fun setMastodonAccountId(mastodonAccountId: String) {
        Identifiers.mastodonAccountId.set(mastodonAccountId)
    }

    override fun setUserAgent(userAgent: String) {
        Identifiers.userAgent.set(userAgent)
    }

    override fun clearLoggedInIdentifiers() {
        Identifiers.mastodonAccountHandle.destroy()
        Identifiers.mastodonAccountId.destroy()
    }
}