package org.mozilla.social.feature.settings.account

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mozilla.social.core.domain.AccountFlow
import org.mozilla.social.core.domain.Logout
import org.mozilla.social.model.Account

class AccountSettingsViewModelTest {
    private lateinit var objUnderTest: AccountSettingsViewModel

    private val logout: Logout = mockk(relaxed = true)
    private val accountFlow: AccountFlow = mockk(relaxed = true)

    private val account = mockk<Account>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { account.accountId } returns "123"
        every { account.acct } returns "acct"
        every { account.avatarUrl } returns "avatarurl"
        every { account.url } returns "url"
        every { accountFlow() } returns flowOf(account)
        objUnderTest = AccountSettingsViewModel(logout, accountFlow)
    }

    @Test
    fun onLogout() {
        objUnderTest.onLogoutClicked()
        verify { logout() }
    }

    @Test
    fun userHeaderState() = runTest {
        val userHeader = objUnderTest.userHeader.first()
        verify { account.toUserHeader() }
        assertThat(userHeader?.accountName).isEqualTo(account.acct)
    }


}