package org.mozilla.social.core.analytics.glean

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mozilla.telemetry.glean.BuildInfo
import mozilla.telemetry.glean.Glean
import org.mozilla.social.common.Version
import org.mozilla.social.core.analytics.core.Analytics
import org.mozilla.social.core.analytics.core.EngagementType
import org.mozilla.social.core.analytics.GleanMetrics.Identifiers
import org.mozilla.social.core.analytics.GleanMetrics.Mobile
import org.mozilla.social.core.analytics.GleanMetrics.Ui
import org.mozilla.social.core.datastore.AppPreferencesDatastore
import java.util.Calendar

internal class GleanAnalytics(
    private val appPreferencesDatastore: AppPreferencesDatastore,
) : Analytics {
    @OptIn(DelicateCoroutinesApi::class)
    override fun initialize(context: Context) {
        val buildInfo = BuildInfo(Version.name, Version.code.toString(), Calendar.getInstance())

        Glean.setLogPings(true)

        CoroutineScope(Main).launch {
            appPreferencesDatastore.allowAnalytics.collectLatest {
                Glean.initialize(
                    applicationContext = context,
                    uploadEnabled = it,
                    buildInfo = buildInfo,
                )
            }
        }

        GlobalScope.launch {
            appPreferencesDatastore.allowAnalytics.collectLatest {
                Glean.setUploadEnabled(it)
            }
        }
    }

    override fun uiEngagement(
        engagementType: EngagementType?,
        engagementValue: String?,
        recommendationId: String?,
        uiAdditionalDetail: String?,
        uiIdentifier: String?,
    ) {
        Ui.engagement.record(
            extra =
                Ui.EngagementExtra(
                    engagementType = engagementType?.value,
                    engagementValue = engagementValue,
                    recommendationId = recommendationId,
                    uiAdditionalDetail = uiAdditionalDetail,
                    uiIdentifier = uiIdentifier,
                ),
        )
    }

    override fun uiImpression(
        recommendationId: String?,
        uiAdditionalDetail: String?,
        uiIdentifier: String?,
    ) {
        Ui.impression.record(
            extra =
                Ui.ImpressionExtra(
                    recommendationId = recommendationId,
                    uiAdditionalDetail = uiAdditionalDetail,
                    uiIdentifier = uiIdentifier,
                ),
        )
    }

    override fun setAdjustDeviceId(adjustDeviceId: String) {
        Identifiers.adjustDeviceId.set(adjustDeviceId)
    }

    override fun setFxaAccountId(fxaAccountId: String) {
        Identifiers.fxaAccountId.set(fxaAccountId)
    }

    override fun setMastodonAccountId(mastodonAccountId: String) {
        Identifiers.mastodonAccountId.set(mastodonAccountId)
    }

    override fun setUserAgent(userAgent: String) {
        Identifiers.userAgent.set(userAgent)
    }

    override fun clearLoggedInIdentifiers() {
        Identifiers.mastodonAccountId.set("")
    }


    override fun appOpened() {
        Mobile.appOpen.record()
    }

    override fun appBackgrounded() {
        Mobile.appBackground.record()
    }
}
