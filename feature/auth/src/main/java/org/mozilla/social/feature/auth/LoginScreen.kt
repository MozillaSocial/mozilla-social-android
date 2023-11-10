package org.mozilla.social.feature.auth

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.component.MoSoBadge
import org.mozilla.social.core.designsystem.component.MoSoButton
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.font.MoSoFonts
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
internal fun LoginScreen(
    viewModel: AuthViewModel = koinViewModel(),
) {
    val configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        HorizontalLoginScreen(
            authInteractions = viewModel,
        )
    } else {
        VerticalLoginScreen(
            authInteractions = viewModel,
        )
    }

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun HorizontalLoginScreen(
    authInteractions: AuthInteractions,
) {
    MoSoSurface(
        color = MoSoTheme.colors.layer2
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            ImageBox(
                modifier = Modifier
                    .weight(1f)
                    .systemBarsPadding(),
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .fillMaxHeight()
                    .background(MoSoTheme.colors.layer1),
            ) {
                LoginBox(
                    modifier = Modifier.align(Alignment.Center),
                    authInteractions = authInteractions,
                )
            }
        }
    }
}

@Composable
private fun VerticalLoginScreen(
    authInteractions: AuthInteractions,
) {
    MoSoSurface(
        color = MoSoTheme.colors.layer2
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ImageBox()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.weight(1f))
            LoginBox(
                modifier = Modifier.weight(1f),
                authInteractions = authInteractions,
            )
        }
    }
}

@Composable
private fun ImageBox(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp),
            text = stringResource(id = R.string.title_text),
            fontSize = 24.sp,
            fontWeight = W700,
            fontFamily = MoSoFonts.zillaSlab,
        )
        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.login_art),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.TopCenter
        )
    }
}

@Composable
private fun LoginBox(
    modifier: Modifier = Modifier,
    authInteractions: AuthInteractions,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MoSoTheme.colors.layer1)
            .padding(MoSoSpacing.lg),
    ) {
        MoSoBadge {
            Text(
                text = stringResource(id = R.string.beta_badge),
                style = MoSoTheme.typography.labelSmall,
            )
        }

        Spacer(modifier = Modifier.height(MoSoSpacing.md))

        Text(
            text = stringResource(id = R.string.intro_title),
            style = MoSoTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(MoSoSpacing.md))
        Text(
            text = stringResource(id = R.string.intro_message),
            style = MoSoTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(24.dp))
        MoSoButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { authInteractions.onSignInClicked(context) }
        ) {
            Text(
                text = stringResource(id = R.string.sign_in_button),
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.choose_server_option),
            style = MoSoTheme.typography.labelSmallLink,
            textDecoration = TextDecoration.Underline,
            color = MoSoTheme.colors.textLink,
        )
    }
}

@Preview
@Composable
internal fun AuthVerticalScreenPreview() {
    MoSoTheme {
        VerticalLoginScreen(
            authInteractions = object : AuthInteractions {},
        )
    }
}

@Preview(heightDp = 400, widthDp = 800)
@Composable
internal fun AuthHorizontalScreenPreview() {
    MoSoTheme {
        HorizontalLoginScreen(
            authInteractions = object : AuthInteractions {},
        )
    }
}