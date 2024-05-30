package social.firefly.core.analytics

import android.content.Context
import social.firefly.core.analytics.core.Analytics

class AppAnalytics internal constructor(private val analytics: Analytics) {

    fun appOpened() = analytics.appOpened()

    fun appBackgrounded() = analytics.appBackgrounded()

    fun initialize(context: Context) = analytics.initialize(context)
}