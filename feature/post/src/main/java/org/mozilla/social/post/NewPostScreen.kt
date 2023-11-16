@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
)

package org.mozilla.social.post

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.mozilla.social.common.LoadState
import org.mozilla.social.common.utils.buildAnnotatedStringForAccountsAndHashtags
import org.mozilla.social.core.designsystem.component.MoSoButton
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.core.designsystem.component.MoSoTextField
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.designsystem.utils.NoIndication
import org.mozilla.social.core.ui.common.TransparentNoTouchOverlay
import org.mozilla.social.core.ui.common.VerticalDivider
import org.mozilla.social.core.ui.common.VisibilityDropDownButton
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.media.MediaUpload
import org.mozilla.social.core.ui.common.transparentTextFieldColors
import org.mozilla.social.core.ui.common.utils.getWindowHeightClass
import org.mozilla.social.feature.post.R
import org.mozilla.social.core.model.ImageState
import org.mozilla.social.core.model.StatusVisibility
import org.mozilla.social.post.NewPostViewModel.Companion.MAX_POLL_COUNT
import org.mozilla.social.post.NewPostViewModel.Companion.MIN_POLL_COUNT
import org.mozilla.social.post.bottombar.BottomBar
import org.mozilla.social.post.bottombar.BottomBarState
import org.mozilla.social.post.media.MediaInteractions
import org.mozilla.social.post.poll.Poll
import org.mozilla.social.post.poll.PollDuration
import org.mozilla.social.post.poll.PollDurationDropDown
import org.mozilla.social.post.poll.PollInteractions
import org.mozilla.social.post.poll.PollStyle
import org.mozilla.social.post.poll.PollStyleDropDown
import org.mozilla.social.post.status.Account
import org.mozilla.social.post.status.AccountSearchBar
import org.mozilla.social.post.status.ContentWarningInteractions
import org.mozilla.social.post.status.HashtagSearchBar
import org.mozilla.social.post.status.StatusInteractions

@Composable
internal fun NewPostScreen(
    replyStatusId: String?,
    viewModel: NewPostViewModel = koinViewModel(parameters = { parametersOf(replyStatusId) })
) {
    val statusText by viewModel.statusText.collectAsStateWithLifecycle()
    val sendButtonEnabled by viewModel.sendButtonEnabled.collectAsStateWithLifecycle()
    val mediaStates by viewModel.mediaStates.collectAsStateWithLifecycle()
    val isSendingPost by viewModel.isSendingPost.collectAsStateWithLifecycle()
    val visibility by viewModel.visibility.collectAsStateWithLifecycle()
    val poll by viewModel.poll.collectAsStateWithLifecycle()
    val contextWarningText by viewModel.contentWarningText.collectAsStateWithLifecycle()
    val accounts by viewModel.accountList.collectAsStateWithLifecycle()
    val hashTags by viewModel.hashtagList.collectAsStateWithLifecycle()
    val inReplyToAccountName by viewModel.inReplyToAccountName.collectAsStateWithLifecycle()
    val userHeaderState by viewModel.userHeaderState.collectAsStateWithLifecycle(initialValue = null)
    val bottomBarState by viewModel.bottomBarState.collectAsStateWithLifecycle()
    NewPostScreen(
        statusText = statusText,
        statusInteractions = viewModel.statusInteractions,
        onPostClicked = viewModel::onPostClicked,
        sendButtonEnabled = sendButtonEnabled,
        imageStates = mediaStates,
        mediaInteractions = viewModel.mediaInteractions,
        isSendingPost = isSendingPost,
        visibility = visibility,
        onVisibilitySelected = viewModel::onVisibilitySelected,
        poll = poll,
        pollInteractions = viewModel.pollInteractions,
        contentWarningText = contextWarningText,
        contentWarningInteractions = viewModel.contentWarningInteractions,
        accounts = accounts,
        hashTags = hashTags,
        inReplyToAccountName = inReplyToAccountName,
        userHeaderState = userHeaderState,
        bottomBarState = bottomBarState,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

data class UserHeaderState(val avatarUrl: String, val displayName: String)

@Composable
private fun NewPostScreen(
    bottomBarState: BottomBarState,
    statusText: TextFieldValue,
    statusInteractions: StatusInteractions,
    onPostClicked: () -> Unit,
    sendButtonEnabled: Boolean,
    imageStates: List<ImageState>,
    mediaInteractions: MediaInteractions,
    isSendingPost: Boolean,
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit,
    poll: Poll?,
    pollInteractions: PollInteractions,
    contentWarningText: String?,
    contentWarningInteractions: ContentWarningInteractions,
    accounts: List<Account>?,
    hashTags: List<String>?,
    inReplyToAccountName: String?,
    userHeaderState: UserHeaderState?,
) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .imePadding()
            .background(MoSoTheme.colors.layer1)
    ) {
        if (getWindowHeightClass() == WindowHeightSizeClass.Compact) {
            Row {
                CompactNewPostScreenContent(
                    statusText = statusText,
                    statusInteractions = statusInteractions,
                    onPostClicked = onPostClicked,
                    sendButtonEnabled = sendButtonEnabled,
                    imageStates = imageStates,
                    mediaInteractions = mediaInteractions,
                    poll = poll,
                    pollInteractions = pollInteractions,
                    contentWarningText = contentWarningText,
                    contentWarningInteractions = contentWarningInteractions,
                    inReplyToAccountName = inReplyToAccountName,
                )
            }
        } else {
            NewPostScreenContent(
                bottomBarState = bottomBarState,
                statusText = statusText,
                statusInteractions = statusInteractions,
                onPostClicked = onPostClicked,
                sendButtonEnabled = sendButtonEnabled,
                imageStates = imageStates,
                mediaInteractions = mediaInteractions,
                visibility = visibility,
                onVisibilitySelected = onVisibilitySelected,
                poll = poll,
                pollInteractions = pollInteractions,
                contentWarningText = contentWarningText,
                contentWarningInteractions = contentWarningInteractions,
                accounts = accounts,
                hashTags = hashTags,
                inReplyToAccountName = inReplyToAccountName,
                userHeaderState = userHeaderState
            )
        }

        if (isSendingPost) {
            TransparentNoTouchOverlay()
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun CompactNewPostScreenContent(
    statusText: TextFieldValue,
    statusInteractions: StatusInteractions,
    onPostClicked: () -> Unit,
    sendButtonEnabled: Boolean,
    imageStates: List<ImageState>,
    mediaInteractions: MediaInteractions,
    poll: Poll?,
    pollInteractions: PollInteractions,
    contentWarningText: String?,
    contentWarningInteractions: ContentWarningInteractions,
    inReplyToAccountName: String?,
) {
    Row {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            MainBox(
                statusText = statusText,
                statusInteractions = statusInteractions,
                imageStates = imageStates,
                mediaInteractions = mediaInteractions,
                poll = poll,
                pollInteractions = pollInteractions,
                contentWarningText = contentWarningText,
                contentWarningInteractions = contentWarningInteractions,
                inReplyToAccountName = inReplyToAccountName,
            )
        }

        PostButton(onPostClicked = onPostClicked, sendButtonEnabled = sendButtonEnabled)
    }
}

@Composable
private fun NewPostScreenContent(
    bottomBarState: BottomBarState,
    statusText: TextFieldValue,
    statusInteractions: StatusInteractions,
    onPostClicked: () -> Unit,
    sendButtonEnabled: Boolean,
    imageStates: List<ImageState>,
    mediaInteractions: MediaInteractions,
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit,
    poll: Poll?,
    pollInteractions: PollInteractions,
    contentWarningText: String?,
    contentWarningInteractions: ContentWarningInteractions,
    accounts: List<Account>?,
    hashTags: List<String>?,
    inReplyToAccountName: String?,
    userHeaderState: UserHeaderState?,
) {
    Column {
        TopBar(
            onPostClicked = onPostClicked,
            sendButtonEnabled = sendButtonEnabled,
        )
        userHeaderState?.let { userHeaderState ->
            UserHeader(
                userHeaderState = userHeaderState,
                visibility = visibility,
                onVisibilitySelected = onVisibilitySelected,
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            MainBox(
                statusText = statusText,
                statusInteractions = statusInteractions,
                imageStates = imageStates,
                mediaInteractions = mediaInteractions,
                poll = poll,
                pollInteractions = pollInteractions,
                contentWarningText = contentWarningText,
                contentWarningInteractions = contentWarningInteractions,
                inReplyToAccountName = inReplyToAccountName,
            )
        }
        accounts?.let {
            AccountSearchBar(accounts = accounts, statusInteractions = statusInteractions)
        }
        hashTags?.let {
            HashtagSearchBar(hashTags = hashTags, statusInteractions = statusInteractions)
        }
        BottomBar(
            bottomBarState = bottomBarState,
            onMediaInserted = mediaInteractions::onMediaInserted,
            pollInteractions = pollInteractions,
            contentWarningInteractions = contentWarningInteractions,
        )
    }


}

@Composable
fun UserHeader(
    userHeaderState: UserHeaderState,
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit
) {
    Row {
        AsyncImage(
            modifier = Modifier
                .padding(horizontal = MoSoSpacing.sm)
                .size(92.dp)
                .clip(CircleShape)
                .border(
                    width = 3.dp,
                    color = MoSoTheme.colors.layer1,
                    shape = CircleShape
                ),
            model = userHeaderState.avatarUrl,
            contentDescription = null,
        )

        Column {
            Text(text = userHeaderState.displayName, style = MoSoTheme.typography.labelMedium)

            VisibilityDropDownButton(
                visibility = visibility,
                onVisibilitySelected = onVisibilitySelected,
            )
        }

    }
}

@Composable
private fun TopBar(
    onPostClicked: () -> Unit,
    sendButtonEnabled: Boolean,
) {
    MoSoCloseableTopAppBar(
        actions = {
            PostButton(onPostClicked = onPostClicked, sendButtonEnabled = sendButtonEnabled)
        }
    )
}

@Composable
private fun PostButton(onPostClicked: () -> Unit, sendButtonEnabled: Boolean) {
    MoSoButton(onClick = onPostClicked, enabled = sendButtonEnabled) {
        Text(
            text = stringResource(id = R.string.post),
            style = MoSoTheme.typography.labelSmall
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MainBox(
    statusText: TextFieldValue,
    statusInteractions: StatusInteractions,
    imageStates: List<ImageState>,
    mediaInteractions: MediaInteractions,
    poll: Poll?,
    pollInteractions: PollInteractions,
    contentWarningText: String?,
    contentWarningInteractions: ContentWarningInteractions,
    inReplyToAccountName: String?,
) {
    val localIndication = LocalIndication.current
    // disable ripple on click for the background
    CompositionLocalProvider(
        LocalIndication provides NoIndication
    ) {
        val keyboard = LocalSoftwareKeyboardController.current

        val textFieldFocusRequester = remember { FocusRequester() }
        MoSoSurface(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    textFieldFocusRequester.requestFocus()
                    keyboard?.show()
                }
        ) {
            // re-enable ripple
            CompositionLocalProvider(
                LocalIndication provides localIndication
            ) {
                LazyColumn {
                    item {
                        InReplyToText(inReplyToAccountName = inReplyToAccountName)
                    }
                    contentWarningText?.let {
                        item {
                            ContentWarningEntry(contentWarningText, contentWarningInteractions)
                        }
                    }

                    item {
                        val highlightColor = MoSoTheme.colors.textLink
                        MoSoTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(textFieldFocusRequester),
                            value = statusText,
                            onValueChange = { statusInteractions.onStatusTextUpdated(it) },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.new_post_text_field_label)
                                )
                            },
                            visualTransformation = {
                                TransformedText(
                                    buildAnnotatedStringForAccountsAndHashtags(
                                        it.text,
                                        SpanStyle(
                                            color = highlightColor
                                        )
                                    ),
                                    OffsetMapping.Identity
                                )
                            }
                        )
                        LaunchedEffect(Unit) {
                            textFieldFocusRequester.requestFocus()
                        }
                    }

                    poll?.let {
                        items(poll.options.size) { index ->
                            PollChoice(
                                index = index,
                                poll = poll,
                                pollInteractions = pollInteractions
                            )
                        }
                        item { PollSettings(poll = poll, pollInteractions = pollInteractions) }
                    }

                    items(imageStates.size) { index ->
                        ImageUploadBox(
                            imageState = imageStates[index],
                            mediaInteractions = mediaInteractions,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InReplyToText(
    inReplyToAccountName: String?,
) {
    if (inReplyToAccountName != null) {
        Row(
            modifier = Modifier.padding(start = 12.dp),
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically),
                painter = MoSoIcons.chatBubbles(),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.padding(start = 8.dp))
            Text(
                text = stringResource(
                    id = R.string.in_reply_to_account_name_label,
                    inReplyToAccountName
                ),
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun ContentWarningEntry(
    contentWarningText: String,
    contentWarningInteractions: ContentWarningInteractions,
) {
    MoSoTextField(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        value = contentWarningText,
        onValueChange = { contentWarningInteractions.onContentWarningTextChanged(it) },
        label = { Text(text = stringResource(id = R.string.content_warning_label)) },
        borderColor = MoSoTheme.colors.borderAccent
    )
}

@Composable
private fun ImageUploadBox(
    imageState: ImageState,
    mediaInteractions: MediaInteractions,
) {
    val outlineShape = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .padding(16.dp)
            .border(
                width = 2.dp,
                color = MoSoTheme.colors.borderPrimary,
                shape = outlineShape
            )
            .clip(
                outlineShape
            )
            .fillMaxWidth(),
    ) {
        MediaUpload(
            uri = imageState.uri,
            loadState = imageState.loadState,
            onRetryClicked = mediaInteractions::onMediaInserted,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (imageState.loadState == LoadState.LOADED) {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = imageState.description,
                    onValueChange = {
                        mediaInteractions.onMediaDescriptionTextUpdated(
                            imageState.uri,
                            it
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.media_alt_text_label)
                        )
                    },
                    colors = transparentTextFieldColors(),
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
            IconButton(
                onClick = {
                    mediaInteractions.onDeleteMediaClicked(imageState.uri)
                }
            ) {
                Icon(
                    MoSoIcons.delete(),
                    stringResource(id = R.string.delete_button_content_description)
                )
            }
        }
    }
}

@Composable
private fun PollChoice(
    index: Int,
    poll: Poll,
    pollInteractions: PollInteractions,
) {
    val deleteEnabled = remember(poll) { poll.options.size > MIN_POLL_COUNT }
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            value = poll.options[index],
            onValueChange = { pollInteractions.onPollOptionTextChanged(index, it) },
            label = {
                Text(
                    text = stringResource(id = R.string.poll_choice_label, index + 1)
                )
            }
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            onClick = {
                pollInteractions.onPollOptionDeleteClicked(index)
            },
            enabled = deleteEnabled
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                painter = MoSoIcons.deleteOutline(),
                contentDescription = stringResource(id = R.string.remove_poll_option_button_content_description),
            )
        }
    }
}

@Composable
private fun PollSettings(
    poll: Poll,
    pollInteractions: PollInteractions,
) {
    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
    ) {
        val addNewOptionEnabled = remember(poll) { poll.options.size < MAX_POLL_COUNT }
        IconButton(
            onClick = {
                pollInteractions.onAddPollOptionClicked()
            },
            enabled = addNewOptionEnabled,
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                painter = MoSoIcons.addCircleOutline(),
                contentDescription = stringResource(id = R.string.add_poll_option_button_content_description),
            )
        }

        Row(
            modifier = Modifier.padding(top = 4.dp)
        ) {
            VerticalDivider(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .height(40.dp)
            )
            PollDurationDropDown(poll = poll, pollInteractions = pollInteractions)
            VerticalDivider(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .height(40.dp)
            )
            PollStyleDropDown(poll = poll, pollInteractions = pollInteractions)
        }
    }
    Row(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .clickable { pollInteractions.onHideCountUntilEndClicked() }
    ) {
        Checkbox(
            modifier = Modifier.align(Alignment.CenterVertically),
            checked = poll.hideTotals,
            onCheckedChange = { pollInteractions.onHideCountUntilEndClicked() },
        )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(id = R.string.poll_option_hide_results),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Preview
@Composable
private fun NewPostScreenPreview() {
    MoSoTheme(
        false
    ) {
        NewPostScreen(
            statusText = TextFieldValue(),
            statusInteractions = object : StatusInteractions {},
            onPostClicked = {},
            sendButtonEnabled = true,
            imageStates = listOf(),
            mediaInteractions = object : MediaInteractions {},
            isSendingPost = false,
            visibility = StatusVisibility.Private,
            onVisibilitySelected = {},
            poll = null,
            pollInteractions = object : PollInteractions {},
            contentWarningText = null,
            contentWarningInteractions = object : ContentWarningInteractions {},
            accounts = null,
            hashTags = null,
            inReplyToAccountName = null,
            userHeaderState = UserHeaderState("", "Barack Obama"),
            bottomBarState = BottomBarState()
        )
    }
}

@Preview
@Composable
private fun NewPostScreenWithPollPreview() {
    MoSoTheme(
        false
    ) {
        NewPostScreen(
            statusText = TextFieldValue(),
            statusInteractions = object : StatusInteractions {},
            onPostClicked = {},
            sendButtonEnabled = true,
            imageStates = listOf(),
            mediaInteractions = object : MediaInteractions {},
            isSendingPost = false,
            visibility = StatusVisibility.Private,
            onVisibilitySelected = {},
            poll = Poll(
                options = listOf("option 1", "option 2"),
                style = PollStyle.SINGLE_CHOICE,
                pollDuration = PollDuration.ONE_DAY,
                hideTotals = false
            ),
            pollInteractions = object : PollInteractions {},
            contentWarningText = null,
            contentWarningInteractions = object : ContentWarningInteractions {},
            accounts = null,
            hashTags = null,
            inReplyToAccountName = null,
            userHeaderState = UserHeaderState("", "Barack Obama"),
            bottomBarState = BottomBarState(),
        )
    }
}

@Preview
@Composable
private fun NewPostScreenWithContentWarningPreview() {
    MoSoTheme(
        false
    ) {
        NewPostScreen(
            statusText = TextFieldValue(),
            statusInteractions = object : StatusInteractions {},
            onPostClicked = {},
            sendButtonEnabled = true,
            imageStates = listOf(),
            mediaInteractions = object : MediaInteractions {},
            isSendingPost = false,
            visibility = StatusVisibility.Private,
            onVisibilitySelected = {},
            poll = null,
            pollInteractions = object : PollInteractions {},
            contentWarningText = "Content is bad",
            contentWarningInteractions = object : ContentWarningInteractions {},
            accounts = null,
            hashTags = null,
            inReplyToAccountName = null,
            userHeaderState = UserHeaderState("", "Barack Obama"),
            bottomBarState = BottomBarState(),
        )
    }
}
