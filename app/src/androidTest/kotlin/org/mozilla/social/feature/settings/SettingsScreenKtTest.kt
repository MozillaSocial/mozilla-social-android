package org.mozilla.social.feature.settings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.mozilla.social.core.designsystem.theme.MoSoTheme

// This is broken
class SettingsScreenKtTest {

@get:Rule
val composeTestRule = createComposeRule()
// use createAndroidComposeRule<YourActivity>() if you need access to
// an activity

    // TODO@DA fix this
    @Test
    fun accountSettingsShow() {
        composeTestRule.setContent {
            MoSoTheme {
                SettingsScreen()
            }
        }

        composeTestRule.onNodeWithText("Account").performClick()

        composeTestRule.onNodeWithText("Profile Settings").assertIsDisplayed()
    }
}