package social.firefly.feature.feed

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.model.AccountTimelineType
import social.firefly.feature.feed.feedModule
import kotlin.test.Test

class CheckModulesTest : KoinTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        feedModule.verify(
            extraTypes = listOf(
                Context::class,
                CoroutineDispatcher::class,
                CoroutineScope::class,
                FeedLocation::class,
                kotlin.Function1::class,
                AccountTimelineType::class,
            ),
        )
    }
}