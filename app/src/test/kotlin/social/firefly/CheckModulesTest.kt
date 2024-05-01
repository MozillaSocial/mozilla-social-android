package social.firefly

import android.content.Context
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import social.firefly.core.model.AccountTimelineType
import social.firefly.core.analytics.FeedLocation
import social.firefly.feature.followers.FollowType
import social.firefly.feature.report.ReportType
import social.firefly.featureModules
import social.firefly.mainModule
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
