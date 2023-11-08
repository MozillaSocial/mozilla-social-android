package org.mozilla.social.core.navigation


import androidx.navigation.NavController
import com.google.common.truth.Truth.assertThat
import io.mockk.impl.annotations.SpyK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.mozilla.social.core.navigation.NavigationDestination.Login.navigateToLoginScreen
import org.mozilla.social.core.navigation.NavigationDestination.EditAccount.navigateToEditAccount
import org.mozilla.social.core.navigation.NavigationDestination.Tabs.navigateToTabs

class NavigationDestinationTest {
    private val navController = mockk<NavController>(relaxed = true)
    private val testUtils = TestUtils()

    @Test
    fun navigateToAccount() {
        val accountId = testUtils.randomIdString()

        with(NavigationDestination.Account(accountId)) {
            navController.navigateToAccount()
        }

        verify {
            navController.navigate("account?accountId=$accountId")
        }
    }

    @Test
    fun accountFullRoute() {
        assertThat(NavigationDestination.Account.fullRoute).isEqualTo("account?accountId={accountId}")
    }

    @Test
    fun navigateToEditAccount() {
        navController.navigateToEditAccount()

        verify { navController.navigate("editAccount") }
    }

    @Test
    fun navigateToAuth() {
        navController.navigateToLoginScreen()

        verify { navController.navigate("auth") }
    }

    @Test
    fun navigateToTabs() {
        navController.navigateToTabs()

        verify { navController.navigate("tabs") }
    }

    @Test
    fun navigateToFollowers() {
        val accountId = testUtils.randomIdString()

        with(NavigationDestination.Followers(accountId)) {
            navController.navigateToFollowers()
        }

        verify {
            navController.navigate("followers?accountId=$accountId")
        }
    }

    @Test
    fun followersFullRoute() {
        assertThat(NavigationDestination.Followers.fullRoute).isEqualTo("followers?accountId={accountId}")
    }

    @Test
    fun navigateToFollowing() {
        val accountId = testUtils.randomIdString()

        with(NavigationDestination.Following(accountId)) {
            navController.navigateToFollowing()
        }

        verify {
            navController.navigate("following?accountId=$accountId")
        }
    }

    @Test
    fun followingFullRoute() {
        assertThat(NavigationDestination.Following.fullRoute).isEqualTo("following?accountId={accountId}")
    }

    @Test
    fun navigateToHashtag() {
        val hashTag = testUtils.randomWordString()

        with(NavigationDestination.HashTag(hashTag)) {
            navController.navigateToHashTag()
        }

        verify {
            navController.navigate("hashtag?hashTagValue=$hashTag")
        }
    }

    @Test
    fun hashtagFullRoute() {
        assertThat(NavigationDestination.HashTag.fullRoute).isEqualTo("hashtag?hashTagValue={hashTagValue}")
    }

    @Test
    fun navigateToNewPost() {
        with(NavigationDestination.NewPost()) {
            navController.navigateToNewPost()
        }

        verify {
            navController.navigate("newPost")
        }
    }

    @Test
    fun newPostFullRoute() {
        assertThat(NavigationDestination.HashTag.fullRoute).isEqualTo("hashtag?hashTagValue={hashTagValue}")
    }

    @Test
    fun navigateToNewPostReply() {
        val replyId = testUtils.randomIdString()

        with(NavigationDestination.NewPost(replyId)) {
            navController.navigateToNewPost()
        }

        verify {
            navController.navigate("newPost?replyStatusId=$replyId")
        }
    }
}