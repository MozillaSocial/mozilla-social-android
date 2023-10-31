package org.mozilla.social.feature.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.core.designsystem.component.MoSoButtonSecondary
import org.mozilla.social.core.designsystem.component.MoSoDivider
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.theme.MoSoTheme

@Composable
internal fun SettingsScreen(
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val isAnalyticsToggled = settingsViewModel.isAnalyticsToggledOn.collectAsState()

    SettingsScreen(
        isAnalyticsToggledOn = isAnalyticsToggled.value,
        settingsViewModel::toggleAnalytics,
        settingsViewModel::logoutUser
    )
}

@Composable
internal fun SettingsScreen(
    isAnalyticsToggledOn: Boolean,
    toggleAnalyticsSwitch: () -> Unit,
    logUserOut: () -> Unit
) {
    MoSoSurface(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()) {
        Column(
            modifier = Modifier
                .padding(top = 24.dp)
        ) {
            SettingsAnalytics(
                isAnalyticsToggledOn = isAnalyticsToggledOn,
                toggleAnalyticsSwitch = toggleAnalyticsSwitch
            )
        }
        Column(verticalArrangement = Arrangement.Bottom) {
            MoSoButtonSecondary(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = { logUserOut }
            ) { Text(text = "logout") }
        }
    }
}

@Composable
fun SettingsGroup(
    @StringRes name: Int,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier
        .padding(start = 16.dp, top = 16.dp, bottom = 8.dp, end = 16.dp)
    ) {
        Text(stringResource(id = name))
        Spacer(modifier = Modifier.height(8.dp))
        MoSoSurface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8),
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingsAnalytics(
    isAnalyticsToggledOn: Boolean,
    toggleAnalyticsSwitch: () -> Unit
) {
    Column {
        SettingsGroup(name = R.string.analytics_group) {
            SettingsSwitch(
                name = R.string.analytics_opt_in_name,
                subtitle = R.string.analytics_opt_in_subtitle,
                state = isAnalyticsToggledOn,
                onClick = toggleAnalyticsSwitch
            )
        }
    }
}

@Suppress("LongParameterList")
@Composable
fun SettingsSwitch(
    @DrawableRes icon: Int? = null,
    @StringRes iconDesc: Int? = null,
    @StringRes name: Int,
    @StringRes subtitle: Int? = null,
    state: Boolean,
    onClick: () -> Unit
) {
    MoSoSurface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Column(
                        modifier = Modifier
                            .weight(9f)
                            .padding(bottom = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (icon != null) {
                                Icon(
                                    painterResource(id = icon),
                                    contentDescription = iconDesc?.let { stringResource(id = it) },
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = stringResource(id = name),
                                modifier = Modifier.padding(top = 8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Start,
                            )
                        }
                        if (subtitle != null) {
                            Text(
                                text = stringResource(id = subtitle),
                                modifier = Modifier.padding(top = 8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Start,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = state,
                        onCheckedChange = { onClick() },
                        modifier = Modifier
                            .padding(end = 16.dp)
                    )
                }
            }
           MoSoDivider()
        }
    }
}

@Composable
private fun SettingsTextLink(
    @DrawableRes icon: Int,
    @StringRes iconDesc: Int,
    @StringRes name: Int,
    onClick: () -> Unit
) {

    MoSoSurface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onClick,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(id = icon),
                        contentDescription = stringResource(id = iconDesc),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = name),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            MoSoDivider()
        }
    }
}

@Composable
private fun LogoutText(
    @StringRes name: Int,
    onClick: () -> Unit
) {

    MoSoSurface(
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = name),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 8.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun SettingsScreenPreview() {
    MoSoTheme {
        SettingsScreen(false, {}, {})
    }
}
