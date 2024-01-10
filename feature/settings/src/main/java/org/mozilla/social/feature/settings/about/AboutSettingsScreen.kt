package org.mozilla.social.feature.settings.about

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
import org.mozilla.social.common.Resource
import org.mozilla.social.core.designsystem.theme.MoSoRadius
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.model.InstanceRule
import org.mozilla.social.core.navigation.navigationModule
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickView
import org.mozilla.social.core.ui.common.account.quickview.AccountQuickViewUiState
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.divider.MoSoDivider
import org.mozilla.social.core.ui.common.error.GenericError
import org.mozilla.social.core.ui.common.loading.MaxSizeLoading
import org.mozilla.social.core.ui.common.text.MediumTextBody
import org.mozilla.social.core.ui.common.text.MediumTextTitle
import org.mozilla.social.core.ui.common.utils.PreviewTheme
import org.mozilla.social.core.ui.htmlcontent.HtmlContent
import org.mozilla.social.core.ui.htmlcontent.HtmlContentInteractions
import org.mozilla.social.feature.settings.R

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

@Composable
private fun AboutSettingsScreen(
    aboutSettingsResource: Resource<AboutSettings>,
    htmlContentInteractions: HtmlContentInteractions,
    aboutInteractions: AboutInteractions,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MoSoCloseableTopAppBar(title = stringResource(id = R.string.about_settings_title))
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
            .padding(MoSoSpacing.lg)
    ) {
        AsyncImage(
            modifier =
            Modifier
                .clip(RoundedCornerShape(MoSoRadius.lg_16_dp))
                .fillMaxWidth()
                .wrapContentHeight(),
            model = aboutSettings.thumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )

        Spacer(modifier = Modifier.height(MoSoSpacing.lg))

        Text(text = aboutSettings.title, style = MoSoTheme.typography.labelLarge)

        Spacer(modifier = Modifier.height(MoSoSpacing.lg))

        Text(
            text = stringResource(id = R.string.decentralized_social_media_powered_by_mastodon),
            style = MoSoTheme.typography.bodyMedium,
        )

        Divider()

        aboutSettings.administeredBy?.let { state ->
            Text(
                text = stringResource(id = R.string.administered_by),
                style = MoSoTheme.typography.titleSmall,
            )
            AccountQuickView(uiState = state)
        }

        Divider()

        aboutSettings.contactEmail?.let { contactEmail ->
            Text(
                text = stringResource(id = R.string.contact_email, contactEmail),
                style = MoSoTheme.typography.bodyMedium,
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

        Spacer(modifier = Modifier.height(MoSoSpacing.xxl))
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
    Spacer(modifier = Modifier.height(MoSoSpacing.lg))
    MoSoDivider()
    Spacer(modifier = Modifier.height(MoSoSpacing.lg))
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
                    title = "mozilla.social",
                    administeredBy =
                    AccountQuickViewUiState(
                        accountId = "",
                        displayName = "Mozilla Social",
                        webFinger = "@social",
                        avatarUrl = "",
                    ),
                    contactEmail = "support@mozilla-social.zendesk.com",
                    extendedDescription =
                    "We’re here to build a social platform that puts " +
                            "people first. Mozilla.social is currently available to a closed " +
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
