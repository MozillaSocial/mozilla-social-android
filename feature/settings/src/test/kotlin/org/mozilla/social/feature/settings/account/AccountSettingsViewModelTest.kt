package org.mozilla.social.feature.settings.account

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test
import org.mozilla.social.common.Resource
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.usecase.mastodon.account.GetDomain
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.auth.Logout
import org.mozilla.social.core.analytics.SettingsAnalytics
import kotlin.test.BeforeTest

class AccountSettingsViewModelTest {
    private lateinit var objUnderTest: AccountSettingsViewModel

    private val accountId = "123"
    private val logout: Logout = mockk(relaxed = true)

    private val getLoggedInUserAccountId: GetLoggedInUserAccountId = mockk(relaxed = true)
    private val accountRepository: AccountRepository = mockk(relaxed = true)
    private val analytics: SettingsAnalytics = mockk(relaxed = true)
    private val getDomain: GetDomain = mockk(relaxed = true)
    private val openLink: OpenLink = mockk(relaxed = true)

    private val account = mockk<Account>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { account.accountId } returns accountId
        every { account.acct } returns "acct"
        every { account.avatarUrl } returns "avatarurl"
        every { account.url } returns "url"
        every { getLoggedInUserAccountId() } returns accountId
        coEvery { accountRepository.getAccount(accountId) } returns account
        objUnderTest =
            AccountSettingsViewModel(
                logout = logout,
                getLoggedInUserAccountId = getLoggedInUserAccountId,
                accountRepository = accountRepository,
                analytics = analytics,
                openLink = openLink,
                getDomain = getDomain,
            )
    }

    @Test
    fun onLogout() {
        objUnderTest.onLogoutClicked()
        verify { logout() }
    }

    @Test
    fun userHeaderState() =
        runTest {
            val userHeader = objUnderTest.userHeader.collectAsList()
            verify { account.toUserHeader() }
            assertThat(userHeader[0]).isInstanceOf(Resource.Loading::class.java)
            assertThat(userHeader[1].data?.accountName).isEqualTo("acct")
        }
}

private fun <T> Flow<T>.collectAsList(): List<T> {
    val data = mutableListOf<T>()
    CoroutineScope(Dispatchers.Unconfined).launch {
        collect {
            data.add(it)
        }
    }
    return data
}
