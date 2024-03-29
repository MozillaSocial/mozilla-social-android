package org.mozilla.social.feature.discover

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import org.mozilla.social.core.model.AccountTimelineType
import kotlin.test.Test

class CheckModulesTest : KoinTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        discoverModule.verify(
            extraTypes = listOf(
                Context::class,
                CoroutineDispatcher::class,
                CoroutineScope::class,
                AccountTimelineType::class,
            ),
        )
    }
}