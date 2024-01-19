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
import androidx.compose.ui.graphics.Color
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
import org.mozilla.social.core.designsystem.icon.MoSoIcons
import org.mozilla.social.core.designsystem.theme.MoSoSpacing
import org.mozilla.social.core.designsystem.theme.MoSoTheme
import org.mozilla.social.core.designsystem.utils.NoIndication
import org.mozilla.social.core.model.ImageState
import org.mozilla.social.core.model.StatusVisibility
import org.mozilla.social.core.ui.common.MoSoSurface
import org.mozilla.social.core.ui.common.TransparentNoTouchOverlay
import org.mozilla.social.core.ui.common.appbar.MoSoCloseableTopAppBar
import org.mozilla.social.core.ui.common.button.MoSoButton
import org.mozilla.social.core.ui.common.divider.MoSoVerticalDivider
import org.mozilla.social.core.ui.common.dropdown.VisibilityDropDownButton
import org.mozilla.social.core.ui.common.media.MediaUpload
import org.mozilla.social.core.ui.common.text.MoSoTextField
import org.mozilla.social.core.ui.common.transparentTextFieldColors
import org.mozilla.social.core.ui.common.utils.getWindowHeightClass
import org.mozilla.social.feature.post.R
import org.mozilla.social.post.NewPostViewModel.Companion.MAX_POLL_COUNT
import org.mozilla.social.post.NewPostViewModel.Companion.MIN_POLL_COUNT
import org.mozilla.social.post.bottombar.BottomBar
import org.mozilla.social.post.bottombar.BottomBarState
import org.mozilla.social.post.media.MediaInteractions
import org.mozilla.social.post.poll.PollUiState
import org.mozilla.social.post.poll.PollDuration
import org.mozilla.social.post.poll.PollDurationDropDown
import org.mozilla.social.post.poll.PollInteractions
import org.mozilla.social.post.poll.PollStyle
import org.mozilla.social.post.poll.PollStyleDropDown
import org.mozilla.social.post.status.AccountSearchBar
import org.mozilla.social.post.status.ContentWarningInteractions
import org.mozilla.social.post.status.HashtagSearchBar
import org.mozilla.social.post.status.StatusInteractions
import org.mozilla.social.post.status.StatusUiState

@Composable
internal fun NewPostScreen(
    replyStatusId: String?,
    viewModel: NewPostViewModel = koinViewModel(parameters = { parametersOf(replyStatusId) }),
) {
    val statusUiState by viewModel.statusDelegate.uiState.collectAsStateWithLifecycle()
    val sendButtonEnabled by viewModel.sendButtonEnabled.collectAsStateWithLifecycle()
    val mediaStates by viewModel.mediaDelegate.imageStates.collectAsStateWithLifecycle()
    val isSendingPost by viewModel.isSendingPost.collectAsStateWithLifecycle()
    val visibility by viewModel.visibility.collectAsStateWithLifecycle()
    val pollUiState by viewModel.pollDelegate.uiState.collectAsStateWithLifecycle()
    val userHeaderState by viewModel.userHeaderState.collectAsStateWithLifecycle(initialValue = null)
    val bottomBarState by viewModel.bottomBarState.collectAsStateWithLifecycle()

    NewPostScreen(
        statusUiState = statusUiState,
        statusInteractions = viewModel.statusDelegate,
        onPostClicked = viewModel::onPostClicked,
        sendButtonEnabled = sendButtonEnabled,
        imageStates = mediaStates,
        mediaInteractions = viewModel.mediaDelegate,
        isSendingPost = isSendingPost,
        visibility = visibility,
        onVisibilitySelected = viewModel::onVisibilitySelected,
        pollUiState = pollUiState,
        pollInteractions = viewModel.pollDelegate,
        contentWarningInteractions = viewModel.statusDelegate,
        userHeaderState = userHeaderState,
        bottomBarState = bottomBarState,
        onUploadImageClicked = viewModel::onUploadImageClicked,
        onUploadMediaClicked = viewModel::onUploadMediaClicked
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

data class UserHeaderState(val avatarUrl: String, val displayName: String)

@Composable
private fun NewPostScreen(
    statusUiState: StatusUiState,
    bottomBarState: BottomBarState,
    statusInteractions: StatusInteractions,
    onPostClicked: () -> Unit,
    sendButtonEnabled: Boolean,
    imageStates: List<ImageState>,
    mediaInteractions: MediaInteractions,
    isSendingPost: Boolean,
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit,
    pollUiState: PollUiState?,
    pollInteractions: PollInteractions,
    contentWarningInteractions: ContentWarningInteractions,
    userHeaderState: UserHeaderState?,
    onUploadImageClicked: () -> Unit,
    onUploadMediaClicked: () -> Unit
) {
    Box(
        modifier =
        Modifier
            .systemBarsPadding()
            .imePadding()
            .background(MoSoTheme.colors.layer1),
    ) {
        if (getWindowHeightClass() == WindowHeightSizeClass.Compact) {
            Row {
                CompactNewPostScreenContent(
                    statusUiState = statusUiState,
                    statusInteractions = statusInteractions,
                    onPostClicked = onPostClicked,
                    sendButtonEnabled = sendButtonEnabled,
                    imageStates = imageStates,
                    mediaInteractions = mediaInteractions,
                    pollUiState = pollUiState,
                    pollInteractions = pollInteractions,
                    contentWarningInteractions = contentWarningInteractions,
                )
            }
        } else {
            NewPostScreenContent(
                statusUiState = statusUiState,
                bottomBarState = bottomBarState,
                statusInteractions = statusInteractions,
                onPostClicked = onPostClicked,
                sendButtonEnabled = sendButtonEnabled,
                imageStates = imageStates,
                mediaInteractions = mediaInteractions,
                visibility = visibility,
                onVisibilitySelected = onVisibilitySelected,
                pollUiState = pollUiState,
                pollInteractions = pollInteractions,
                contentWarningInteractions = contentWarningInteractions,
                userHeaderState = userHeaderState,
                onUploadImageClicked = onUploadImageClicked,
                onUploadMediaClicked = onUploadMediaClicked
            )
        }

        if (isSendingPost) {
            TransparentNoTouchOverlay()
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Composable
private fun CompactNewPostScreenContent(
    statusUiState: StatusUiState,
    statusInteractions: StatusInteractions,
    onPostClicked: () -> Unit,
    sendButtonEnabled: Boolean,
    imageStates: List<ImageState>,
    mediaInteractions: MediaInteractions,
    pollUiState: PollUiState?,
    pollInteractions: PollInteractions,
    contentWarningInteractions: ContentWarningInteractions,
) {
    Row {
        Box(
            modifier =
                Modifier
                    .weight(1f),
        ) {
            MainBox(
                statusUiState = statusUiState,
                statusInteractions = statusInteractions,
                imageStates = imageStates,
                mediaInteractions = mediaInteractions,
                pollUiState = pollUiState,
                pollInteractions = pollInteractions,
                contentWarningInteractions = contentWarningInteractions,
            )
        }

        PostButton(
            modifier = Modifier.padding(end = 16.dp),
            onPostClicked = onPostClicked,
            sendButtonEnabled = sendButtonEnabled,
        )
    }
}

@Composable
private fun NewPostScreenContent(
    statusUiState: StatusUiState,
    bottomBarState: BottomBarState,
    statusInteractions: StatusInteractions,
    onPostClicked: () -> Unit,
    sendButtonEnabled: Boolean,
    imageStates: List<ImageState>,
    mediaInteractions: MediaInteractions,
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit,
    pollUiState: PollUiState?,
    pollInteractions: PollInteractions,
    contentWarningInteractions: ContentWarningInteractions,
    userHeaderState: UserHeaderState?,
    onUploadImageClicked: () -> Unit,
    onUploadMediaClicked: () -> Unit,
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
            modifier =
                Modifier
                    .weight(1f),
        ) {
            MainBox(
                statusUiState = statusUiState,
                statusInteractions = statusInteractions,
                imageStates = imageStates,
                mediaInteractions = mediaInteractions,
                pollUiState = pollUiState,
                pollInteractions = pollInteractions,
                contentWarningInteractions = contentWarningInteractions,
            )
        }
        statusUiState.accountList?.let {
            AccountSearchBar(accounts = statusUiState.accountList, statusInteractions = statusInteractions)
        }
        statusUiState.hashtagList?.let {
            HashtagSearchBar(hashTags = statusUiState.hashtagList, statusInteractions = statusInteractions)
        }
        BottomBar(
            bottomBarState = bottomBarState,
            onMediaInserted = mediaInteractions::onMediaInserted,
            pollInteractions = pollInteractions,
            contentWarningInteractions = contentWarningInteractions,
            onUploadImageClicked = onUploadImageClicked,
            onUploadMediaClicked = onUploadMediaClicked
        )
    }
}

@Composable
fun UserHeader(
    userHeaderState: UserHeaderState,
    visibility: StatusVisibility,
    onVisibilitySelected: (StatusVisibility) -> Unit,
) {
    Row {
        AsyncImage(
            modifier =
                Modifier
                    .padding(horizontal = MoSoSpacing.sm)
                    .size(92.dp)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        color = MoSoTheme.colors.layer1,
                        shape = CircleShape,
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
        },
    )
}

@Composable
private fun PostButton(
    onPostClicked: () -> Unit,
    sendButtonEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    MoSoButton(
        modifier = modifier,
        onClick = onPostClicked,
        enabled = sendButtonEnabled
    ) {
        Text(
            text = stringResource(id = R.string.post),
            style = MoSoTheme.typography.labelSmall,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MainBox(
    statusUiState: StatusUiState,
    statusInteractions: StatusInteractions,
    imageStates: List<ImageState>,
    mediaInteractions: MediaInteractions,
    pollUiState: PollUiState?,
    pollInteractions: PollInteractions,
    contentWarningInteractions: ContentWarningInteractions,
) {
    val localIndication = LocalIndication.current
    // disable ripple on click for the background
    CompositionLocalProvider(
        LocalIndication provides NoIndication,
    ) {
        val keyboard = LocalSoftwareKeyboardController.current

        val textFieldFocusRequester = remember { FocusRequester() }
        MoSoSurface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clickable {
                        keyboard?.show()
                    },
        ) {
            // re-enable ripple
            CompositionLocalProvider(
                LocalIndication provides localIndication,
            ) {
                LazyColumn {
                    item {
                        InReplyToText(inReplyToAccountName = statusUiState.inReplyToAccountName)
                    }
                    statusUiState.contentWarningText?.let {
                        item {
                            ContentWarningEntry(statusUiState.contentWarningText, contentWarningInteractions)
                        }
                    }

                    item {
                        val highlightColor = MoSoTheme.colors.textLink
                        MoSoTextField(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .focusRequester(textFieldFocusRequester),
                            value = statusUiState.statusText,
                            onValueChange = { statusInteractions.onStatusTextUpdated(it) },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.new_post_text_field_label),
                                )
                            },
                            visualTransformation = {
                                TransformedText(
                                    buildAnnotatedStringForAccountsAndHashtags(
                                        it.text,
                                        SpanStyle(
                                            color = highlightColor,
                                        ),
                                    ),
                                    OffsetMapping.Identity,
                                )
                            },
                        )
                        LaunchedEffect(Unit) {
                            textFieldFocusRequester.requestFocus()
                        }
                    }

                    pollUiState?.let {
                        items(pollUiState.options.size) { index ->
                            PollChoice(
                                index = index,
                                pollUiState = pollUiState,
                                pollInteractions = pollInteractions,
                            )
                        }
                        item { PollSettings(pollUiState = pollUiState, pollInteractions = pollInteractions) }
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
private fun InReplyToText(inReplyToAccountName: String?) {
    if (inReplyToAccountName != null) {
        Row(
            modifier = Modifier.padding(start = 12.dp),
        ) {
            Icon(
                modifier =
                    Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically),
                painter = MoSoIcons.chatBubbles(),
                contentDescription = "",
            )
            Spacer(modifier = Modifier.padding(start = 8.dp))
            Text(
                text =
                    stringResource(
                        id = R.string.in_reply_to_account_name_label,
                        inReplyToAccountName,
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
        modifier =
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        value = contentWarningText,
        onValueChange = { contentWarningInteractions.onContentWarningTextChanged(it) },
        label = { Text(text = stringResource(id = R.string.content_warning_label)) },
        borderColor = MoSoTheme.colors.borderAccent,
    )
}

@Composable
private fun ImageUploadBox(
    imageState: ImageState,
    mediaInteractions: MediaInteractions,
) {
    val outlineShape = RoundedCornerShape(12.dp)
    Column(
        modifier =
            Modifier
                .padding(16.dp)
                .border(
                    width = 2.dp,
                    color = MoSoTheme.colors.borderPrimary,
                    shape = outlineShape,
                )
                .clip(
                    outlineShape,
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
                MoSoTextField(
                    modifier = Modifier.weight(1f),
                    value = imageState.description,
                    onValueChange = {
                        mediaInteractions.onMediaDescriptionTextUpdated(
                            imageState.uri,
                            it,
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.media_alt_text_label),
                        )
                    },
                    colors = transparentTextFieldColors(),
                    borderColor = Color.Transparent,
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
            IconButton(
                onClick = {
                    mediaInteractions.onDeleteMediaClicked(imageState.uri)
                },
            ) {
                Icon(
                    MoSoIcons.delete(),
                    stringResource(id = R.string.delete_button_content_description),
                )
            }
        }
    }
}

@Composable
private fun PollChoice(
    index: Int,
    pollUiState: PollUiState,
    pollInteractions: PollInteractions,
) {
    val deleteEnabled = remember(pollUiState) { pollUiState.options.size > MIN_POLL_COUNT }
    Row(
        modifier =
            Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
    ) {
        OutlinedTextField(
            modifier =
                Modifier
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
    pollUiState: PollUiState,
    pollInteractions: PollInteractions,
) {
    Row(
        modifier =
            Modifier
                .padding(start = 8.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
    ) {
        val addNewOptionEnabled = remember(pollUiState) { pollUiState.options.size < MAX_POLL_COUNT }
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

@Preview
@Composable
private fun NewPostScreenPreview() {
    MoSoTheme(
        false,
    ) {
        NewPostScreen(
            statusUiState = StatusUiState(
                statusText = TextFieldValue(),
                contentWarningText = null,
                accountList = null,
                hashtagList = null,
                inReplyToAccountName = null,
            ),
            statusInteractions = object : StatusInteractions {},
            onPostClicked = {},
            sendButtonEnabled = true,
            imageStates = listOf(),
            mediaInteractions = object : MediaInteractions {},
            isSendingPost = false,
            visibility = StatusVisibility.Private,
            onVisibilitySelected = {},
            pollUiState = null,
            pollInteractions = object : PollInteractions {},
            contentWarningInteractions = object : ContentWarningInteractions {},
            userHeaderState = UserHeaderState("", "Barack Obama"),
            bottomBarState = BottomBarState(),
            onUploadImageClicked = {},
            onUploadMediaClicked = {},
        )
    }
}

@Preview
@Composable
private fun NewPostScreenWithPollPreview() {
    MoSoTheme(
        false,
    ) {
        NewPostScreen(
            statusUiState = StatusUiState(
                statusText = TextFieldValue(),
                contentWarningText = null,
                accountList = null,
                hashtagList = null,
                inReplyToAccountName = null,
            ),
            statusInteractions = object : StatusInteractions {},
            onPostClicked = {},
            sendButtonEnabled = true,
            imageStates = listOf(),
            mediaInteractions = object : MediaInteractions {},
            isSendingPost = false,
            visibility = StatusVisibility.Private,
            onVisibilitySelected = {},
            pollUiState =
                PollUiState(
                    options = listOf("option 1", "option 2"),
                    style = PollStyle.SINGLE_CHOICE,
                    pollDuration = PollDuration.ONE_DAY,
                    hideTotals = false,
                ),
            pollInteractions = object : PollInteractions {},
            contentWarningInteractions = object : ContentWarningInteractions {},
            userHeaderState = UserHeaderState("", "Barack Obama"),
            bottomBarState = BottomBarState(),
            onUploadImageClicked = {},
            onUploadMediaClicked = {},
        )
    }
}

@Preview
@Composable
private fun NewPostScreenWithContentWarningPreview() {
    MoSoTheme(
        false,
    ) {
        NewPostScreen(
            statusUiState = StatusUiState(
                statusText = TextFieldValue(),
                contentWarningText = "Content is bad",
                accountList = null,
                hashtagList = null,
                inReplyToAccountName = null,
            ),
            statusInteractions = object : StatusInteractions {},
            onPostClicked = {},
            sendButtonEnabled = true,
            imageStates = listOf(),
            mediaInteractions = object : MediaInteractions {},
            isSendingPost = false,
            visibility = StatusVisibility.Private,
            onVisibilitySelected = {},
            pollUiState = null,
            pollInteractions = object : PollInteractions {},
            contentWarningInteractions = object : ContentWarningInteractions {},
            userHeaderState = UserHeaderState("", "Barack Obama"),
            bottomBarState = BottomBarState(),
            onUploadImageClicked = {},
            onUploadMediaClicked = {},
        )
    }
}
