package org.mozilla.social.core.domain

import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.mozilla.social.common.appscope.AppScope
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import kotlin.test.BeforeTest
import kotlin.test.Test

class LogoutTest {

    lateinit var objUnderTest: Logout

    private val userPreferencesDatastore: UserPreferencesDatastore = mockk(relaxed = true)
    private val socialDatabase: SocialDatabase = mockk(relaxed = true)
    private  val analytics: Analytics = mockk(relaxed = true)
    private val ioDispatcher = UnconfinedTestDispatcher()
    private val appScope: AppScope = mockk(relaxed = true)

    @BeforeTest
    fun setup() {
        objUnderTest = Logout(
            userPreferencesDatastore = userPreferencesDatastore,
            socialDatabase = socialDatabase,
            analytics = analytics,
            ioDispatcher = ioDispatcher,
            appScope = appScope,
        )
    }

    @Test
    fun logoutCallsCorrectFunctions() {
        objUnderTest()

        coVerify {
            appScope.reset()
            userPreferencesDatastore.clearData()
            socialDatabase.clearAllTables()
            analytics.clearLoggedInIdentifiers()

        }
    }
}