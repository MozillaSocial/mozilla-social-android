package org.mozilla.social

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import org.mozilla.social.core.model.TimelineType
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
                    TimelineType::class,
                ),
        )
    }
}
