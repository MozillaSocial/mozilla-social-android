package org.mozilla.social.core.analytics.glean

import android.content.Context
import mozilla.telemetry.glean.BuildInfo
import mozilla.telemetry.glean.Glean
import org.mozilla.social.core.analytics.GleanMetrics.Identifiers
import org.mozilla.social.core.analytics.GleanMetrics.Ui
import java.util.Calendar

interface Analytics {
    fun initialize(context: Context)

    fun uiEngagement()

    fun uiImpression()

    fun setAdjustDeviceId()

    fun setFxaAccountId()

    fun setMastodonAccountHandle()

    fun setMastodonInstanceDomain()

    fun setUserAgent()
}

class GleanAnalytics : Analytics {
    override fun initialize(context: Context) {
        val buildInfo = BuildInfo("1", "1", Calendar.getInstance())

        Glean.setLogPings(true)
        Glean.setDebugViewTag("Android-Test")

        Glean.initialize(
            applicationContext = context,
            uploadEnabled = true,
            buildInfo = buildInfo
        )
    }

    override fun uiEngagement() {
        Ui.engagement.record(extra = Ui.EngagementExtra())
    }

    override fun uiImpression() {
        Ui.impression.record(extra = Ui.ImpressionExtra())
    }

    override fun setAdjustDeviceId() {
        Identifiers.adjustDeviceId
    }

    override fun setFxaAccountId() {
        Identifiers.fxaAccountId
    }

    override fun setMastodonAccountHandle() {
        Identifiers.mastodonAccountHandle
    }

    override fun setMastodonInstanceDomain() {
        Identifiers.mastodonInstanceDomain
    }

    override fun setUserAgent() {
        Identifiers.userAgent
    }
}