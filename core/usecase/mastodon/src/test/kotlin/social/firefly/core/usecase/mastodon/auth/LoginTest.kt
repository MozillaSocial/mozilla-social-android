package social.firefly.core.usecase.mastodon.auth

import android.content.Intent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import social.firefly.core.analytics.AppAnalytics
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.navigation.usecases.OpenLink
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.repository.mastodon.OauthRepository
import social.firefly.core.test.TestUtils
import social.firefly.core.test.fakes.fakeApplication
import social.firefly.core.usecase.mastodon.auth.Login.Companion.AUTHORIZATION_CODE
import social.firefly.core.usecase.mastodon.auth.Login.Companion.AUTH_SCHEME
import social.firefly.core.usecase.mastodon.auth.Login.Companion.CLIENT_NAME
import social.firefly.core.usecase.mastodon.auth.Login.Companion.CODE
import social.firefly.core.usecase.mastodon.auth.Login.Companion.REDIRECT_URI
import social.firefly.core.usecase.mastodon.auth.Login.Companion.SCOPES
import kotlin.test.BeforeTest
import kotlin.test.assertNotNull

class LoginTest {
    private val testUtils = TestUtils()
    private lateinit var objUnderTest: Login

    private val oauthRepository: OauthRepository = mockk(relaxed = true)
    private val accountRepository: AccountRepository = mockk(relaxed = true)
    private val userPreferencesDatastore: UserPreferencesDatastoreManager = mockk(relaxed = true)
    private val appRepository: AppRepository = mockk(relaxed = true)
    private val analytics: AppAnalytics = mockk(relaxed = true)
    private val openLink: OpenLink = mockk(relaxed = true)
    private val logout: Logout = mockk(relaxed = true)

    private val userCode: String = testUtils.randomIdString()
    private val domain = "firefly.firefly"
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
                userPreferencesDatastoreManager = userPreferencesDatastore,
                appRepository = appRepository,
                analytics = analytics,
                openLink = openLink,
                logout = logout,
            )
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

            coVerify {
                logout()
            }
        }

    @Test
    fun verifyUserCredentialsThrowsError_userIsLoggedOut() =
        runTest {
            coEvery { accountRepository.verifyUserCredentials() } throws Exception()

            coVerify { logout() }
        }

    @Test
    fun saveAnalyticsCredentialsThrowsError_userIsLoggedOut() =
        runTest {
            coEvery { analytics.setMastodonAccountId(any()) } throws Exception()

            coVerify { logout() }
        }
}
