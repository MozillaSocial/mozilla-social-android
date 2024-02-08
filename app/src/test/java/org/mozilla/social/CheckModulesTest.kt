package org.mozilla.social

import android.content.Context
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import org.mozilla.social.core.model.AccountTimelineType
import org.mozilla.social.core.analytics.FeedLocation
import org.mozilla.social.feature.followers.FollowType
import org.mozilla.social.feature.report.ReportType
import kotlin.test.Test

class CheckModulesTest : KoinTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        featureModules.verify(
            extraTypes =
                listOf(
                    Context::class,
                    CoroutineDispatcher::class,
                    StateFlow::class,
                    Function0::class,
                    Function1::class,
                    ReportType::class,
                    List::class,
                    Boolean::class,
                    FollowType::class,
                    CoroutineScope::class,
                    AccountTimelineType::class,
                    WorkerParameters::class,
                    FeedLocation::class,
                ),
        )

        mainModule.verify(
            extraTypes = listOf(
                Context::class,
                CoroutineDispatcher::class,
                CoroutineScope::class,
            )
        )
    }
}
