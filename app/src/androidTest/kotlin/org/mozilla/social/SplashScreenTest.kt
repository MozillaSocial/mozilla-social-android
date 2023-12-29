package org.mozilla.social


import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val cdSignInOrSignUpButton = composeTestRule.activity
        .getString(R.string.cd_sign_in_button)
    @Test
    fun splashScreenAppears() {
        composeTestRule
            .onNodeWithContentDescription(cdSignInOrSignUpButton)
            .assertIsDisplayed()
    }

    @Test
    fun clickingSignInLoadsLoginScreen() {
        composeTestRule
            .onNodeWithContentDescription(cdSignInOrSignUpButton)
            .performClick()
    }

}