package org.mozilla.social.post.poll

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.designsystem.utils.NoRipple
import org.mozilla.social.core.ui.common.MoSoCheckBox
import org.mozilla.social.core.ui.common.button.MoSoButtonSecondary
import org.mozilla.social.core.ui.common.divider.MoSoDivider
import org.mozilla.social.core.ui.common.text.MoSoTextField
import org.mozilla.social.core.ui.common.text.SmallTextLabel
import org.mozilla.social.core.ui.common.utils.PreviewTheme
import org.mozilla.social.feature.post.R
import org.mozilla.social.post.NewPostViewModel

@Composable
internal fun Poll(
    pollUiState: PollUiState,
    pollInteractions: PollInteractions,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        pollUiState.options.forEachIndexed { index, _ ->
            PollChoice(
                index = index,
                pollUiState = pollUiState,
                pollInteractions = pollInteractions,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        MoSoButtonSecondary(onClick = pollInteractions::onAddPollOptionClicked) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = MoSoIcons.plus(),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            SmallTextLabel(text = stringResource(id = R.string.poll_add_choice_button))
        }
    }
}

@Composable
private fun PollChoice(
    index: Int,
    pollUiState: PollUiState,
    pollInteractions: PollInteractions,
    modifier: Modifier = Modifier,
) {
    val deleteEnabled = remember(pollUiState) { pollUiState.options.size > NewPostViewModel.MIN_POLL_COUNT }
    Row(
        modifier = modifier,
    ) {
        MoSoTextField(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            value = pollUiState.options[index],
            onValueChange = { pollInteractions.onPollOptionTextChanged(index, it) },
            label = {
                Text(
                    text = stringResource(id = R.string.poll_choice_label, index + 1),
                )
            },
        )
        IconButton(
            modifier =
            Modifier
                .align(Alignment.CenterVertically),
            onClick = {
                pollInteractions.onPollOptionDeleteClicked(index)
            },
            enabled = deleteEnabled,
        ) {
            Icon(
                modifier =
                Modifier
                    .width(24.dp)
                    .height(24.dp),
                painter = MoSoIcons.trash(),
                contentDescription = stringResource(id = R.string.remove_poll_option_button_content_description),
            )
        }
    }
}

@Composable
internal fun PollBar(
    pollUiState: PollUiState,
    pollInteractions: PollInteractions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(56.dp)
    ) {
        MoSoDivider()
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MoSoTheme.colors.borderPrimary,
                        shape = CircleShape,
                    )
                    .size(32.dp),
                onClick = pollInteractions::onNewPollClicked
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = MoSoIcons.x(),
                    contentDescription = stringResource(id = R.string.close_poll_content_description)
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            PollDurationDropDown(
                pollUiState = pollUiState,
                pollInteractions = pollInteractions,
            )

            Spacer(modifier = Modifier.width(32.dp))

            PollStyleDropDown(
                pollUiState = pollUiState,
                pollInteractions = pollInteractions,
            )

            Spacer(modifier = Modifier.width(32.dp))

            HideUntilEndCheckbox(pollUiState = pollUiState, pollInteractions = pollInteractions)

            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
private fun HideUntilEndCheckbox(
    pollUiState: PollUiState,
    pollInteractions: PollInteractions,
) {
    NoRipple {
        Row(
            modifier = Modifier
                .clickable { pollInteractions.onHideCountUntilEndClicked() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MoSoCheckBox(
                checked = pollUiState.hideTotals,
                onCheckedChange = { pollInteractions.onHideCountUntilEndClicked() },
            )
            SmallTextLabel(text = stringResource(id = R.string.poll_option_hide_results),)
        }
    }
}

@Preview
@Composable
private fun PollPreview() {
    PreviewTheme {
        Poll(
            pollUiState = PollUiState(
                options = listOf("option 1", "option 2"),
                style = PollStyle.SINGLE_CHOICE,
                pollDuration = PollDuration.ONE_DAY,
                hideTotals = false,
            ),
            pollInteractions = object : PollInteractions {},
        )
    }
}

@Preview
@Composable
private fun PollBarPreview() {
    PreviewTheme {
        PollBar(
            pollUiState = PollUiState(
                options = listOf("option 1", "option 2"),
                style = PollStyle.SINGLE_CHOICE,
                pollDuration = PollDuration.ONE_DAY,
                hideTotals = false,
            ),
            pollInteractions = object : PollInteractions {},
        )
    }
}