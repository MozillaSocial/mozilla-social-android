package org.mozilla.social.feature.settings.contentpreferences.blockedusers

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.mozilla.social.core.repository.mastodon.BlocksRepository
import org.mozilla.social.core.usecase.mastodon.account.BlockAccount
import org.mozilla.social.core.usecase.mastodon.account.UnblockAccount
import kotlin.test.BeforeTest
import kotlin.test.Test

class BlockedUsersViewModelTest {
    private lateinit var objUnderTest: BlockedUsersViewModel

    private val repository: BlocksRepository = mockk()
    private val blockAccount: BlockAccount = mockk(relaxed = true)
    private val unblockAccount: UnblockAccount = mockk(relaxed = true)
    private val accountId = "123"

    @BeforeTest
    fun setup() {
        objUnderTest = BlockedUsersViewModel(
            blockAccount = blockAccount,
            unblockAccount = unblockAccount,
            repository = repository
        )
    }


    @Test
    fun `test onBlockButtonClicked`() {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns UnconfinedTestDispatcher()
        coEvery  { unblockAccount(accountId) } returns Unit

        objUnderTest.onBlockButtonClicked("123")

        coVerify { unblockAccount(accountId) }
    }
}