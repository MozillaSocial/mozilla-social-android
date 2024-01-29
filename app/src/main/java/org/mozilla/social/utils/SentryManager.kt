package org.mozilla.social.utils

import android.content.Context
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import io.sentry.android.timber.SentryTimberIntegration
import org.mozilla.social.BuildConfig

object SentryManager {

    fun initialize(
        context: Context,
    ) {
        SentryAndroid.init(context) { options ->
            options.apply {
                setDiagnosticLevel(SentryLevel.ERROR)
                dsn = BuildConfig.sentryDsn
                isDebug = BuildConfig.DEBUG
                environment = BuildConfig.BUILD_TYPE
                isEnableUserInteractionTracing = true
                isAttachScreenshot = false
                isAttachViewHierarchy = true
                sampleRate = 1.0
                profilesSampleRate = 1.0
                if (!BuildConfig.DEBUG) {
                    addIntegration(
                        SentryTimberIntegration(
                            minEventLevel = SentryLevel.ERROR,
                            minBreadcrumbLevel = SentryLevel.INFO
                        )
                    )
                }
            }
        }
    }
}