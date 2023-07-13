package org.mozilla.social

import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import org.mozilla.social.di.module.appModule

class CheckModulesTest : KoinTest {

    @Test
    fun checkAllModules() {
        appModule.verify()
    }
}