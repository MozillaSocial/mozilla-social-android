package org.mozilla.social.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.component.MoSoBadge
import org.mozilla.social.core.designsystem.component.MoSoButton
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
internal fun LoginScreen(
    viewModel: AuthViewModel = koinViewModel(),
) {
    val defaultUrl = viewModel.defaultUrl.collectAsState().value
    val context = LocalContext.current

    LoginScreen(
        defaultUrl = defaultUrl,
        onLoginClicked = { domain ->
            viewModel.onLoginClicked(
                context = context,
                domain = domain
            )
        }
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun LoginScreen(
    defaultUrl: String,
    onLoginClicked: (String) -> Unit
) {
    MoSoSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .systemBarsPadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 12.dp),
                    text = stringResource(id = R.string.title_text),
                    style = MoSoTheme.typography.titleLarge,
                    fontWeight = W700,
                )
                Image(
                    painter = painterResource(id = R.drawable.login_art),
                    contentDescription = ""
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                    onClick = { /*TODO*/ }
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


            Spacer(modifier = Modifier.weight(2f))
        }
    }
}

@Preview
@Composable
internal fun AuthScreenPreview() {
    MoSoTheme {
        LoginScreen(defaultUrl = "example.com", onLoginClicked = {})
    }
}
