package social.firefly.feature.settings.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import social.firefly.common.Resource
import social.firefly.core.designsystem.theme.FfRadius
import social.firefly.core.designsystem.theme.FfSpacing
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.model.InstanceRule
import social.firefly.core.navigation.navigationModule
import social.firefly.core.ui.common.account.quickview.AccountQuickView
import social.firefly.core.ui.common.account.quickview.AccountQuickViewUiState
import social.firefly.core.ui.common.appbar.FfCloseableTopAppBar
import social.firefly.core.ui.common.divider.FfDivider
import social.firefly.core.ui.common.error.GenericError
import social.firefly.core.ui.common.loading.MaxSizeLoading
import social.firefly.core.ui.common.text.MediumTextBody
import social.firefly.core.ui.common.text.MediumTextTitle
import social.firefly.core.ui.common.utils.PreviewTheme
import social.firefly.core.ui.htmlcontent.HtmlContent
import social.firefly.core.ui.htmlcontent.HtmlContentInteractions
import social.firefly.feature.settings.R

@Composable
internal fun AboutSettingsScreen(
    aboutSettingsViewModel: AboutSettingsViewModel = koinViewModel()
) {
    val aboutSettings: Resource<AboutSettings> by aboutSettingsViewModel.aboutSettings.collectAsStateWithLifecycle()

    AboutSettingsScreen(
        aboutSettingsResource = aboutSettings,
        htmlContentInteractions = aboutSettingsViewModel,
        aboutInteractions = aboutSettingsViewModel,
    )

    LaunchedEffect(Unit) {
        aboutSettingsViewModel.onScreenViewed()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutSettingsScreen(
    aboutSettingsResource: Resource<AboutSettings>,
    htmlContentInteractions: HtmlContentInteractions,
    aboutInteractions: AboutInteractions,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FfCloseableTopAppBar(title = stringResource(id = R.string.about_settings_title))
        when (aboutSettingsResource) {
            is Resource.Loading -> {
                MaxSizeLoading()
            }

            is Resource.Loaded -> {
                LoadedScreen(
                    aboutSettings = aboutSettingsResource.data,
                    htmlContentInteractions = htmlContentInteractions,
                    onOpenSourceLicensesClicked = aboutInteractions::onOpenSourceLicensesClicked,
                )
            }

            is Resource.Error -> {
                GenericError(
                    modifier = Modifier.fillMaxSize(),
                    onRetryClicked = aboutInteractions::onRetryClicked,
                )
            }
        }
    }
}

@Composable
private fun LoadedScreen(
    aboutSettings: AboutSettings,
    htmlContentInteractions: HtmlContentInteractions,
    onOpenSourceLicensesClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(FfSpacing.lg)
    ) {
        AsyncImage(
            modifier =
            Modifier
                .clip(RoundedCornerShape(FfRadius.lg_16_dp))
                .fillMaxWidth()
                .wrapContentHeight(),
            model = aboutSettings.thumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )

        Spacer(modifier = Modifier.height(FfSpacing.lg))

        Text(text = aboutSettings.title, style = FfTheme.typography.labelLarge)

        Spacer(modifier = Modifier.height(FfSpacing.lg))

        Text(
            text = stringResource(id = R.string.decentralized_social_media_powered_by_mastodon),
            style = FfTheme.typography.bodyMedium,
        )

        Divider()

        aboutSettings.administeredBy?.let { state ->
            Text(
                text = stringResource(id = R.string.administered_by),
                style = FfTheme.typography.titleSmall,
            )
            AccountQuickView(uiState = state)
        }

        Divider()

        aboutSettings.contactEmail?.let { contactEmail ->
            Text(
                text = stringResource(id = R.string.contact_email, contactEmail),
                style = FfTheme.typography.bodyMedium,
            )
        }

        Divider()

        aboutSettings.extendedDescription?.let { description ->
            HtmlContent(
                htmlText = description,
                htmlContentInteractions = htmlContentInteractions,
                maximumLineCount = Int.MAX_VALUE,
            )

        }

        Divider()

        ServerRules(aboutSettings.rules)

        Divider()

        OpenSourceLicenses(onClick = onOpenSourceLicensesClicked)

        Spacer(modifier = Modifier.height(FfSpacing.xxl))
    }
}

@Composable
private fun ServerRules(rules: List<InstanceRule>) {
    MediumTextTitle(text = stringResource(id = R.string.server_community_rules))

    for (rule in rules) {
        MediumTextBody(text = rule.text)
    }
}

@Composable
private fun OpenSourceLicenses(onClick: () -> Unit) {
    MediumTextBody(
        modifier = Modifier.clickable(onClick = onClick),
        text = stringResource(id = R.string.open_source_licenses),
        textDecoration = TextDecoration.Underline,
    )
}

@Composable
private fun Divider() {
    Spacer(modifier = Modifier.height(FfSpacing.lg))
    FfDivider()
    Spacer(modifier = Modifier.height(FfSpacing.lg))
}

@Preview
@Composable
fun AboutSettingsScreenPreview() {
    PreviewTheme(
        modules = listOf(navigationModule)
    ) {
        AboutSettingsScreen(
            aboutInteractions = object : AboutInteractions {
                override fun onScreenViewed() = Unit
                override fun onOpenSourceLicensesClicked() = Unit
                override fun onRetryClicked() = Unit
            },
            htmlContentInteractions = object : HtmlContentInteractions {},
            aboutSettingsResource = Resource.Loaded(
                AboutSettings(
                    title = "firefly.firefly",
                    administeredBy =
                    AccountQuickViewUiState(
                        accountId = "",
                        displayName = "Mozilla Social",
                        webFinger = "@firefly",
                        avatarUrl = "",
                    ),
                    contactEmail = "support@firefly-firefly.zendesk.com",
                    extendedDescription =
                    "We’re here to build a firefly platform that puts " +
                            "people first. Mozilla.firefly is currently available to a closed " +
                            "beta group as we experiment, gain input from participants, learn, " +
                            "and improve the experience. Eventually we hope to build a safe, " +
                            "well-organized space within Mastodon that is open to all audiences.",
                    thumbnailUrl = "",
                    rules = listOf(),
                )
            ),
        )
    }
}

data class AboutSettings(
    val title: String,
    val administeredBy: AccountQuickViewUiState?,
    val contactEmail: String?,
    val extendedDescription: String?,
    val thumbnailUrl: String?,
    val rules: List<InstanceRule>,
)
