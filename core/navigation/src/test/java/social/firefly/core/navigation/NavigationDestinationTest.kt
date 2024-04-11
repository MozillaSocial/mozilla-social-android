package social.firefly.core.navigation

import androidx.navigation.NavController
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import social.firefly.core.navigation.NavigationDestination.Auth.navigateToAuthFlow
import social.firefly.core.navigation.NavigationDestination.EditAccount.navigateToEditAccount
import social.firefly.core.navigation.NavigationDestination.Tabs.navigateToTabs
import social.firefly.core.test.TestUtils

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
        navController.navigateToAuthFlow()

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
        val displayName = "person"
        val startingTab = NavigationDestination.Followers.StartingTab.FOLLOWERS

        with(NavigationDestination.Followers(accountId, displayName, startingTab)) {
            navController.navigateToFollowing()
        }

        verify {
            navController.navigate(
                "followers?" +
                        "accountId=$accountId" +
                        "&displayName=$displayName" +
                        "&startingTab=${startingTab.value}"
            )
        }
    }

    @Test
    fun followersFullRoute() {
        assertThat(NavigationDestination.Followers.fullRoute).isEqualTo(
            "followers?" +
                    "accountId={accountId}" +
                    "&displayName={displayName}" +
                    "&startingTab={startingTab}"
        )
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

        with(NavigationDestination.NewPost(replyStatusId = replyId)) {
            navController.navigateToNewPost()
        }

        verify {
            navController.navigate("newPost?replyStatusId=$replyId")
        }
    }

    @Test
    fun navigateToSettings() {
        with(NavigationDestination.Settings) {
            navController.navigateToSettings()
        }

        verify {
            navController.navigate("settings")
        }
    }
}
