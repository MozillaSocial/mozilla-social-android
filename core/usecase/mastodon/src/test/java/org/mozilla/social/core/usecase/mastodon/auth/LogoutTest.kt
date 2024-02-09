package org.mozilla.social.core.usecase.mastodon.auth

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.mozilla.social.common.appscope.AppScope
import org.mozilla.social.core.analytics.AppAnalytics
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate
import kotlin.test.BeforeTest
import kotlin.test.Test

class LogoutTest {
    lateinit var objUnderTest: Logout

    private val userPreferencesDatastore: UserPreferencesDatastore = mockk(relaxed = true)
    private val analytics: AppAnalytics = mockk(relaxed = true)
    private val ioDispatcher = UnconfinedTestDispatcher()
    private val appScope: AppScope = mockk(relaxed = true)
    private val databaseDelegate = mockk<DatabaseDelegate>(relaxed = true)

    @BeforeTest
    fun setup() {
        objUnderTest =
            Logout(
                userPreferencesDatastore = userPreferencesDatastore,
                analytics = analytics,
                ioDispatcher = ioDispatcher,
                appScope = appScope,
                databaseDelegate = databaseDelegate,
            )
    }

    @Test
    fun logoutCallsCorrectFunctions() {
        objUnderTest()

        coVerify {
            appScope.reset()
            userPreferencesDatastore.clearData()
            databaseDelegate.clearAllTables()
            analytics.clearLoggedInIdentifiers()
        }
    }
}
