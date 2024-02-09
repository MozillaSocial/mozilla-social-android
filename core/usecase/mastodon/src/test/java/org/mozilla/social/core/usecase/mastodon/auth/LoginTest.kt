package org.mozilla.social.core.usecase.mastodon.auth

import android.content.Intent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mozilla.social.core.analytics.AppAnalytics
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.repository.mastodon.AppRepository
import org.mozilla.social.core.repository.mastodon.OauthRepository
import org.mozilla.social.core.test.TestUtils
import org.mozilla.social.core.test.fakes.fakeApplication
import org.mozilla.social.core.usecase.mastodon.auth.Login.Companion.AUTHORIZATION_CODE
import org.mozilla.social.core.usecase.mastodon.auth.Login.Companion.AUTH_SCHEME
import org.mozilla.social.core.usecase.mastodon.auth.Login.Companion.CLIENT_NAME
import org.mozilla.social.core.usecase.mastodon.auth.Login.Companion.CODE
import org.mozilla.social.core.usecase.mastodon.auth.Login.Companion.REDIRECT_URI
import org.mozilla.social.core.usecase.mastodon.auth.Login.Companion.SCOPES
import kotlin.test.BeforeTest
import kotlin.test.assertNotNull

class LoginTest {
    private val testUtils = TestUtils()
    private lateinit var objUnderTest: Login

    private val oauthRepository: OauthRepository = mockk(relaxed = true)
    private val accountRepository: AccountRepository = mockk(relaxed = true)
    private val userPreferencesDatastore: UserPreferencesDatastore = mockk(relaxed = true)
    private val appRepository: AppRepository = mockk(relaxed = true)
    private val analytics: AppAnalytics = mockk(relaxed = true)
    private val openLink: OpenLink = mockk(relaxed = true)
    private val logout: Logout = mockk(relaxed = true)

    private val userCode: String = testUtils.randomIdString()
    private val domain = "mozilla.social"
    private val clientId = testUtils.randomWordString()
    private val clientSecret = testUtils.randomWordString()
    private val application = fakeApplication(clientId = clientId, clientSecret = clientSecret)
    private val fakeToken = testUtils.randomWordString()
    private val intent: Intent =
        mockk {
            every { data } returns
                mockk {
                    every { this@mockk.toString() } returns "$AUTH_SCHEME?CODE=asdf"
                    every { getQueryParameter(CODE) } returns userCode
                }
        }

    @BeforeTest
    fun setup() {
        coEvery {
            appRepository.createApplication(
                clientName = CLIENT_NAME,
                redirectUris = REDIRECT_URI,
                scopes = SCOPES,
            )
        } returns application

        coEvery {
            oauthRepository.fetchOAuthToken(
                clientId,
                clientSecret,
                redirectUri = REDIRECT_URI,
                code = userCode,
                grantType = AUTHORIZATION_CODE,
            )
        } returns fakeToken

        objUnderTest =
            Login(
                oauthRepository = oauthRepository,
                accountRepository = accountRepository,
                userPreferencesDatastore = userPreferencesDatastore,
                appRepository = appRepository,
                analytics = analytics,
                openLink = openLink,
                logout = logout,
            )
    }

    @Test
    fun invokeOpensCustomTabWithCorrectInfo() =
        runTest {
            objUnderTest.invoke(domain)

            verify { openLink(any()) }
            coVerify { userPreferencesDatastore.saveDomain(domain) }
        }

    @Test
    fun onNewIntentReceived_oAuthTokenSaved() =
        runTest {
            objUnderTest.invoke(domain)
            objUnderTest.onNewIntentReceived(intent)

            coVerify {
                oauthRepository.fetchOAuthToken(
                    clientId,
                    clientSecret,
                    redirectUri = REDIRECT_URI,
                    code = userCode,
                    grantType = AUTHORIZATION_CODE,
                )

                userPreferencesDatastore.saveAccessToken(fakeToken)
            }
        }

    @Test
    fun onNewIntentReceived_accountIdSaved() =
        runTest {
            objUnderTest.invoke(domain)
            objUnderTest.onNewIntentReceived(intent)

            coVerify {
                oauthRepository.fetchOAuthToken(
                    clientId,
                    clientSecret,
                    redirectUri = REDIRECT_URI,
                    code = userCode,
                    grantType = AUTHORIZATION_CODE,
                )

                userPreferencesDatastore.saveAccessToken(fakeToken)
            }
        }

    @Test
    fun saveDomainThrowsError_userIsLoggedOut() =
        runTest {
            coEvery { userPreferencesDatastore.saveDomain(any()) } throws Exception()
            var exception: Login.LoginFailedException? = null

            try {
                objUnderTest.invoke(domain)
            } catch (e: Login.LoginFailedException) {
                exception = e
            }

            assertNotNull(exception)

            coVerify {
                logout()
            }
        }

    @Test
    fun createApplicationThrowsError_userIsLoggedOut() =
        runTest {
            coEvery { appRepository.createApplication(any(), any(), any()) } throws Exception()
            var exception: Login.LoginFailedException? = null

            try {
                objUnderTest.invoke(domain)
            } catch (e: Login.LoginFailedException) {
                exception = e
            }

            assertNotNull(exception)

            coVerify {
                logout()
            }
        }

    @Test
    fun openCustomTabThrowsError_userIsLoggedOut() =
        runTest {
            coEvery { openLink(any()) } throws Exception()
            var exception: Login.LoginFailedException? = null

            try {
                objUnderTest.invoke(domain)
            } catch (e: Login.LoginFailedException) {
                exception = e
            }

            assertNotNull(exception)

            coVerify {
                logout()
            }
        }

    @Test
    fun `user is logged out when fetch oauth token throws error`() =
        runTest {
            coEvery {
                oauthRepository.fetchOAuthToken(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            } throws Exception()
            objUnderTest.onNewIntentReceived(intent)

            coVerify {
                logout()
            }
        }

    @Test
    fun saveAccessTokenThrowsError_userIsLoggedOut() =
        runTest {
            coEvery { userPreferencesDatastore.saveAccessToken(any()) } throws Exception()

            objUnderTest.onNewIntentReceived(intent)

            coVerify { logout() }
        }

    @Test
    fun verifyUserCredentialsThrowsError_userIsLoggedOut() =
        runTest {
            coEvery { accountRepository.verifyUserCredentials() } throws Exception()

            objUnderTest.onNewIntentReceived(intent)

            coVerify { logout() }
        }

    @Test
    fun saveAccountIdThrowsError_userIsLoggedOut() =
        runTest {
            coEvery { userPreferencesDatastore.saveAccountId(any()) } throws Exception()

            objUnderTest.onNewIntentReceived(intent)

            coVerify { logout() }
        }

    @Test
    fun saveAnalyticsCredentialsThrowsError_userIsLoggedOut() =
        runTest {
            coEvery { analytics.setMastodonAccountId(any()) } throws Exception()

            objUnderTest.onNewIntentReceived(intent)

            coVerify { logout() }
        }
}
