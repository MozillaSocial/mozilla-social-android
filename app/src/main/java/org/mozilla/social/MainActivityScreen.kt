package org.mozilla.social

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.mozilla.social.core.designsystem.theme.MozillaSocialTheme
import org.mozilla.social.feature.auth.AUTH_ROUTE
import org.mozilla.social.feature.auth.authScreen

@Composable
fun MainActivityScreen() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = AUTH_ROUTE) {
        composable("main") { MainScreen(onLoginButtonClicked = { navController.navigate("timeline") }) }
        composable("timeline") { TimelineScreen() }
        authScreen(
            onSignedIn = { navController.navigate("timeline") }
        )
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, onLoginButtonClicked: () -> Unit) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(text = "MainScreen")
        Button(onClick = onLoginButtonClicked) {
            Text(text = "login to mozilla.social")
        }
    }
}

@Composable
fun TimelineScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(text = "TimelineScreen")
    }
}

@Preview
@Composable
fun mainActivityScreenPreview() {
    MozillaSocialTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainActivityScreen()
        }
    }

}