package social.firefly.core.network.mastodon

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import kotlin.test.Test

class CheckModulesTest : KoinTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        mastodonNetworkModule.verify(
            extraTypes = listOf(
                Context::class,
                CoroutineDispatcher::class,
                CoroutineScope::class,
            ),
        )
    }
}