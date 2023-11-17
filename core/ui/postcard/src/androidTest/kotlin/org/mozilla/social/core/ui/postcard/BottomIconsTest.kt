@file:OptIn(DelicateCoroutinesApi::class)

package org.mozilla.social.core.ui.postcard

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.navigation.Event
import org.mozilla.social.core.navigation.EventRelay
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.ui.postcard.util.PostCardUiStateTestObject1

class BottomIconsTest : KoinComponent {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val postCardDelegate: PostCardDelegate by inject { parametersOf(GlobalScope) }

    private val eventRelay: EventRelay by inject()

    @Before
    fun setup() {
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().context)
            modules(postCardModule)
        }

        composeTestRule.setContent {
            MoSoTheme {
                PostCard(
                    post = PostCardUiStateTestObject1.test1,
                    postCardInteractions = postCardDelegate,
                )
            }
        }
    }

    @Test
    fun replyButtonClickTest() {
        var latestEvent: Event? = null
        GlobalScope.launch {
            eventRelay.navigationEvents.collect {
                latestEvent = it
            }
        }

        composeTestRule.onNodeWithTag(
            testTag = "reply",
            useUnmergedTree = true,
        ).performClick()

        composeTestRule.waitUntil { latestEvent != null }

        assert(
            latestEvent == Event.NavigateToDestination(
                NavigationDestination.NewPost(PostCardUiStateTestObject1.statusId)
            )
        )
    }
}