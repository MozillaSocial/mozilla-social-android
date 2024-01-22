package org.mozilla.social.post.poll

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import org.mozilla.social.core.ui.common.button.MoSoButtonSecondary
import org.mozilla.social.core.ui.common.divider.MoSoDivider
import org.mozilla.social.core.ui.common.divider.MoSoVerticalDivider
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

        PollSettings(pollUiState = pollUiState, pollInteractions = pollInteractions)
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
private fun PollSettings(
    pollUiState: PollUiState,
    pollInteractions: PollInteractions,
) {
    Row(
        modifier =
        Modifier
            .padding(start = 8.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
    ) {
        val addNewOptionEnabled = remember(pollUiState) { pollUiState.options.size < NewPostViewModel.MAX_POLL_COUNT }
        IconButton(
            onClick = {
                pollInteractions.onAddPollOptionClicked()
            },
            enabled = addNewOptionEnabled,
        ) {
            Icon(
                modifier =
                Modifier
                    .width(40.dp)
                    .height(40.dp),
                painter = MoSoIcons.addCircleOutline(),
                contentDescription = stringResource(id = R.string.add_poll_option_button_content_description),
            )
        }

        Row(
            modifier = Modifier.padding(top = 4.dp),
        ) {
            MoSoVerticalDivider(
                modifier =
                Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .height(40.dp),
            )
            PollDurationDropDown(pollUiState = pollUiState, pollInteractions = pollInteractions)
            MoSoVerticalDivider(
                modifier =
                Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .height(40.dp),
            )
            PollStyleDropDown(pollUiState = pollUiState, pollInteractions = pollInteractions)
        }
    }
    Row(
        modifier =
        Modifier
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .clickable { pollInteractions.onHideCountUntilEndClicked() },
    ) {
        Checkbox(
            modifier = Modifier.align(Alignment.CenterVertically),
            checked = pollUiState.hideTotals,
            onCheckedChange = { pollInteractions.onHideCountUntilEndClicked() },
        )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(id = R.string.poll_option_hide_results),
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.width(8.dp))
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
        Row {

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