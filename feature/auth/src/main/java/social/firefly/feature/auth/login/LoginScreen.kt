package social.firefly.feature.auth.login

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
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import social.firefly.core.designsystem.font.FfFonts
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.FfBadge
import social.firefly.core.ui.common.FfSurface
import social.firefly.core.ui.common.button.FfButton
import social.firefly.core.ui.common.text.MediumTextLabel
import social.firefly.core.ui.common.utils.getWindowHeightClass
import social.firefly.core.ui.common.utils.getWindowWidthClass
import social.firefly.feature.auth.R

@Composable
internal fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LoginScreen(
        uiState = uiState,
        loginInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun LoginScreen(
    uiState: LoginUiState,
    loginInteractions: LoginInteractions,
) {
    FfSurface(
        modifier = Modifier.fillMaxSize(),
        color = FfTheme.colors.layer2,
    ) {
        val heightClass = getWindowHeightClass()
        val widthClass = getWindowWidthClass()
        when {
            heightClass == WindowHeightSizeClass.Compact && widthClass != WindowWidthSizeClass.Compact ||
                    heightClass == WindowHeightSizeClass.Medium && widthClass == WindowWidthSizeClass.Expanded -> {
                HorizontalLoginScreen(
                    uiState = uiState,
                    loginInteractions = loginInteractions,
                )
            }

            else -> {
                VerticalLoginScreen(
                    uiState = uiState,
                    loginInteractions = loginInteractions,
                )
            }
        }
    }
}

@Composable
private fun HorizontalLoginScreen(
    uiState: LoginUiState,
    loginInteractions: LoginInteractions,
) {
    Row(
        modifier =
        Modifier
            .fillMaxSize(),
    ) {
        ImageBox(
            modifier =
            Modifier
                .weight(1f)
                .systemBarsPadding(),
        )
        Box(
            modifier =
            Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .fillMaxHeight()
                .background(FfTheme.colors.layer1),
        ) {
            LoginBox(
                modifier = Modifier.align(Alignment.Center),
                uiState = uiState,
                loginInteractions = loginInteractions,
            )
        }
    }
}

@Composable
private fun VerticalLoginScreen(
    uiState: LoginUiState,
    loginInteractions: LoginInteractions,
) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ImageBox()
    }
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        LoginBox(
            modifier = Modifier.weight(1f),
            uiState = uiState,
            loginInteractions = loginInteractions,
        )
    }
}

@Composable
private fun ImageBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
    ) {
        Text(
            modifier =
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp),
            text = stringResource(id = R.string.title_text),
            fontSize = 24.sp,
            fontWeight = W700,
            fontFamily = FfFonts.petiteFormalScript,
        )
        Image(
            modifier =
            Modifier
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.login_art),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.TopCenter,
        )
    }
}

@Composable
private fun LoginBox(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    loginInteractions: LoginInteractions,
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .background(FfTheme.colors.layer1)
            .padding(FfSpacing.lg)
            .semantics(mergeDescendants = true) {},
    ) {
        FfBadge {
            Text(
                text = stringResource(id = R.string.alpha_badge),
                style = FfTheme.typography.labelSmall,
            )
        }

        Spacer(modifier = Modifier.height(FfSpacing.md))

        Text(
            text = stringResource(id = R.string.intro_title),
            style = FfTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(FfSpacing.md))
        Text(
            text = stringResource(id = R.string.intro_message),
            style = FfTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(24.dp))
        FfButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { loginInteractions.onChooseServerClicked() },
            enabled = !uiState.isLoading,
        ) {
            MediumTextLabel(text = stringResource(id = R.string.choose_server_option))
        }
    }
}

@Preview
@Composable
internal fun AuthVerticalScreenPreview() {
    FfTheme {
        LoginScreen(
            uiState = LoginUiState(),
            loginInteractions = object : LoginInteractions {},
        )
    }
}

@Preview(heightDp = 400, widthDp = 800)
@Composable
internal fun AuthHorizontalScreenPreview() {
    FfTheme {
        LoginScreen(
            uiState = LoginUiState(),
            loginInteractions = object : LoginInteractions {},
        )
    }
}

@Preview
@Composable
internal fun AuthScreenLoadingPreview() {
    FfTheme {
        LoginScreen(
            uiState =
            LoginUiState(
                isLoading = true,
            ),
            loginInteractions = object : LoginInteractions {},
        )
    }
}
