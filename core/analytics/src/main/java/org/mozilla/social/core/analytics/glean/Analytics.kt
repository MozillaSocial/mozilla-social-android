package org.mozilla.social.core.analytics.glean

import android.content.Context
import mozilla.telemetry.glean.BuildInfo
import mozilla.telemetry.glean.Glean
import org.mozilla.social.core.analytics.GleanMetrics.App
import java.util.Calendar

interface Analytics {
    fun initialize(context: Context) {
    }
}

class GleanAnalytics : Analytics {
    override fun initialize(context: Context) {
        val buildInfo = BuildInfo("1", "1", Calendar.getInstance())

        Glean.setLogPings(true)
        Glean.setDebugViewTag("Tim-Test")

        Glean.initialize(
            applicationContext = context,
            uploadEnabled = true,
            buildInfo = buildInfo
        )

        App.opened.record()
    }
}