package org.mozilla.social.core.ui.postcard

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.navigation.Event
import org.mozilla.social.core.navigation.EventRelay
import org.mozilla.social.core.navigation.NavigationDestination

class PostCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val postCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java
    ) { parametersOf(GlobalScope) }

    private val eventRelay: EventRelay by KoinJavaComponent.inject(
        EventRelay::class.java
    )

    @Before
    fun setup() {
        startKoin {
            androidLogger()
            androidContext(InstrumentationRegistry.getInstrumentation().context)
            modules(
                postCardModule,
            )
        }
    }

    @Test
    fun testReplyClicked() {
        var latestEvent: Event? = null
        GlobalScope.launch {
            eventRelay.navigationEvents.collect {
                latestEvent = it
            }
        }

        composeTestRule.setContent {
            MoSoTheme {
                PostCard(
                    post = PostCardUiState(
                        "statusId",
                        topRowMetaDataUiState = TopRowMetaDataUiState(
                            iconType = TopRowIconType.BOOSTED,
                            text = StringFactory.literal("test")
                        ),
                        mainPostCardUiState = MainPostCardUiState(
                            url = "google.com",
                            pollUiState = null,
                            username = "Test",
                            statusTextHtml = "test",
                            mediaAttachments = emptyList(),
                            profilePictureUrl = "",
                            postTimeSince = StringFactory.literal("test"),
                            accountName = StringFactory.literal("test"),
                            replyCount = 0L,
                            boostCount = 0L,
                            favoriteCount = 0L,
                            statusId = "statusId",
                            userBoosted = false,
                            isFavorited = false,
                            accountId = "id",
                            mentions = emptyList(),
                            previewCard = null,
                            isUsersPost = false,
                            isBeingDeleted = false,
                            contentWarning = ""
                        )
                    ),
                    postCardInteractions = postCardDelegate,
                )
            }
        }

        composeTestRule.onNodeWithTag(
            testTag = "reply",
            useUnmergedTree = true,
        ).performClick()

        composeTestRule.waitUntil { latestEvent != null }

        assert(latestEvent == Event.NavigateToDestination(
            NavigationDestination.NewPost("statusId")
        ))
    }
}