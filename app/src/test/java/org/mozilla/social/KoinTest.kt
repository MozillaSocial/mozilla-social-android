package org.mozilla.social

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import kotlin.test.Test

class CheckModulesTest : KoinTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        appModules.verify()
    }
}