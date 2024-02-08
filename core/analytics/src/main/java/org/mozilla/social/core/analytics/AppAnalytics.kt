package org.mozilla.social.core.analytics

import android.content.Context
import org.mozilla.social.core.analytics.core.Analytics

class AppAnalytics internal constructor(private val analytics: Analytics) {

    fun appOpened() = analytics.appOpened()

    fun appBackgrounded() = analytics.appBackgrounded()

    fun initialize(context: Context) = analytics.initialize(context)

    fun clearLoggedInIdentifiers() = analytics.clearLoggedInIdentifiers()

    fun setMastodonAccountId(mastodonAccountId: String) = analytics.setMastodonAccountId(mastodonAccountId)
}