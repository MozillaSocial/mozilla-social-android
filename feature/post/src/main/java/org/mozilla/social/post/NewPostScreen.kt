@file:OptIn(
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import org.mozilla.social.core.ui.common.button.MoSoButtonContentPadding
import org.mozilla.social.core.ui.common.dropdown.VisibilityDropDownButton
import org.mozilla.social.core.ui.common.media.AttachmentMedia
import org.mozilla.social.core.ui.common.text.MoSoTextField
import org.mozilla.social.core.ui.common.text.SmallTextLabel
import org.mozilla.social.core.ui.common.transparentTextFieldColors
import org.mozilla.social.core.ui.common.utils.getWindowHeightClass
import org.mozilla.social.feature.post.R
import org.mozilla.social.post.bottombar.BottomBar
import org.mozilla.social.post.bottombar.BottomBarState
import org.mozilla.social.post.media.MediaInteractions
import org.mozilla.social.post.poll.Poll
import org.mozilla.social.post.poll.PollBar
import org.mozilla.social.post.poll.PollUiState
import org.mozilla.social.post.poll.PollDuration
import org.mozilla.social.post.poll.PollInteractions
import org.mozilla.social.post.poll.PollStyle
import org.mozilla.social.post.status.AccountSearchBar
import org.mozilla.social.post.status.ContentWarningInteractions
import org.mozilla.social.post.status.HashtagSearchBar
import org.mozilla.social.post.status.StatusInteractions
import org.mozilla.social.post.status.StatusUiState

@Composable
internal fun NewPostScreen(
    replyStatusId: String?,
    editStatusId: String?,
    viewModel: NewPostViewModel = koinViewModel(parameters = { parametersOf(replyStatusId, editStatusId) }),
) {
    val statusUiState by viewModel.statusDelegate.uiState.collectAsStateWithLifecycle()
    val mediaStates by viewModel.mediaDelegate.imageStates.collectAsStateWithLifecycle()
    val pollUiState by viewModel.pollDelegate.uiState.collectAsStateWithLifecycle()
    val newPostUiState by viewModel.newPostUiState.collectAsStateWithLifecycle()

    NewPostScreen(
        statusUiState = statusUiState,
        imageStates = mediaStates,
        pollUiState = pollUiState,
        newPostUiState = newPostUiState,
        statusInteractions = viewModel.statusDelegate,
        mediaInteractions = viewModel.mediaDelegate,
        pollInteractions = viewModel.pollDelegate,
        contentWarningInteractions = viewModel.statusDelegate,
        newPostInteractions = viewModel,
    )

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }
}

@Composable
private fun NewPostScreen(
    statusUiState: StatusUiState,
    imageStates: List<ImageState>,
    pollUiState: PollUiState?,
    newPostUiState: NewPostUiState,
    statusInteractions: StatusInteractions,
    mediaInteractions: MediaInteractions,
    pollInteractions: PollInteractions,
    contentWarningInteractions: ContentWarningInteractions,
    newPostInteractions: NewPostInteractions,
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
                    newPostUiState = newPostUiState,
                    imageStates = imageStates,
                    pollUiState = pollUiState,
                    statusInteractions = statusInteractions,
                    mediaInteractions = mediaInteractions,
                    pollInteractions = pollInteractions,
                    contentWarningInteractions = contentWarningInteractions,
                    newPostInteractions = newPostInteractions,
                )
            }
        } else {
            NewPostScreenContent(
                statusUiState = statusUiState,
                pollUiState = pollUiState,
                imageStates = imageStates,
                newPostUiState = newPostUiState,
                statusInteractions = statusInteractions,
                mediaInteractions = mediaInteractions,
                pollInteractions = pollInteractions,
                contentWarningInteractions = contentWarningInteractions,
                newPostInteractions = newPostInteractions,
            )
        }

        if (newPostUiState.isSendingPost) {
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
    newPostUiState: NewPostUiState,
    imageStates: List<ImageState>,
    pollUiState: PollUiState?,
    statusInteractions: StatusInteractions,
    mediaInteractions: MediaInteractions,
    pollInteractions: PollInteractions,
    contentWarningInteractions: ContentWarningInteractions,
    newPostInteractions: NewPostInteractions,
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

        SubmitButton(
            modifier = Modifier.padding(end = 16.dp),
            onPostClicked = if (!statusUiState.editStatusId.isNullOrBlank())
                newPostInteractions::onEditClicked else newPostInteractions::onPostClicked,
            sendButtonEnabled = newPostUiState.sendButtonEnabled,
            buttonText = if (!statusUiState.editStatusId.isNullOrBlank())
                R.string.edit else R.string.post
        )
    }
}

@Composable
private fun NewPostScreenContent(
    statusUiState: StatusUiState,
    pollUiState: PollUiState?,
    imageStates: List<ImageState>,
    newPostUiState: NewPostUiState,
    statusInteractions: StatusInteractions,
    mediaInteractions: MediaInteractions,
    pollInteractions: PollInteractions,
    contentWarningInteractions: ContentWarningInteractions,
    newPostInteractions: NewPostInteractions,
) {
    Column {
        TopBar(
            statusUiState = statusUiState,
            onPostClicked = newPostInteractions::onPostClicked,
            onEditClicked = newPostInteractions::onEditClicked,
            sendButtonEnabled = newPostUiState.sendButtonEnabled,
        )
        newPostUiState.userHeaderState?.let { userHeaderState ->
            UserHeader(
                userHeaderState = userHeaderState,
                visibility = newPostUiState.visibility,
                onVisibilitySelected = newPostInteractions::onVisibilitySelected,
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
        pollUiState?.let {
            PollBar(pollUiState = pollUiState, pollInteractions = pollInteractions)
        }
        BottomBar(
            bottomBarState = newPostUiState.bottomBarState,
            onMediaInserted = mediaInteractions::uploadMedia,
            pollInteractions = pollInteractions,
            contentWarningInteractions = contentWarningInteractions,
            onUploadImageClicked = newPostInteractions::onUploadImageClicked,
            onUploadMediaClicked = newPostInteractions::onUploadMediaClicked,
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

            Spacer(modifier = Modifier.height(4.dp))

            VisibilityDropDownButton(
                visibility = visibility,
                onVisibilitySelected = onVisibilitySelected,
            )
        }
    }
}

@Composable
private fun TopBar(
    statusUiState: StatusUiState,
    onPostClicked: () -> Unit,
    onEditClicked: () -> Unit,
    sendButtonEnabled: Boolean,
) {
    MoSoCloseableTopAppBar(
        actions = {
            SubmitButton(
                modifier = Modifier.padding(end = 16.dp),
                onPostClicked = if (!statusUiState.editStatusId.isNullOrBlank())
                    onEditClicked else onPostClicked,
                sendButtonEnabled = sendButtonEnabled,
                buttonText = if (!statusUiState.editStatusId.isNullOrBlank())
                    R.string.edit else R.string.post
            )
        },
    )
}

@Composable
private fun SubmitButton(
    onPostClicked: () -> Unit,
    sendButtonEnabled: Boolean,
    modifier: Modifier = Modifier,
    buttonText: Int = R.string.post
) {
    MoSoButton(
        modifier = modifier,
        onClick = onPostClicked,
        enabled = sendButtonEnabled,
        contentPadding = MoSoButtonContentPadding.small,
    ) {
        SmallTextLabel(text = stringResource(id = buttonText))
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
                        item {
                            Poll(
                                modifier = Modifier.padding(16.dp),
                                pollUiState = pollUiState,
                                pollInteractions = pollInteractions,
                            )
                        }
                    }

                    items(imageStates.size) { index ->
                        AttachmentMediaBox(
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
private fun AttachmentMediaBox(
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
        AttachmentMedia(
            uri = imageState.uri,
            loadState = imageState.loadState,
            fileType = imageState.fileType,
            onRetryClicked = mediaInteractions::uploadMedia,
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
            newPostUiState = NewPostUiState(
                sendButtonEnabled = true,
                isSendingPost = false,
                visibility = StatusVisibility.Private,
                userHeaderState = UserHeaderState("", "Barack Obama"),
                bottomBarState = BottomBarState(),
            ),
            statusInteractions = object : StatusInteractions {},
            imageStates = listOf(),
            mediaInteractions = object : MediaInteractions {},
            pollUiState = null,
            pollInteractions = object : PollInteractions {},
            contentWarningInteractions = object : ContentWarningInteractions {},
            newPostInteractions = NewPostInteractionsNoOp,
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
            newPostUiState = NewPostUiState(
                sendButtonEnabled = true,
                isSendingPost = false,
                visibility = StatusVisibility.Private,
                userHeaderState = UserHeaderState("", "Barack Obama"),
                bottomBarState = BottomBarState(),
            ),
            statusInteractions = object : StatusInteractions {},
            imageStates = listOf(),
            mediaInteractions = object : MediaInteractions {},
            pollUiState =
                PollUiState(
                    options = listOf("option 1", "option 2"),
                    style = PollStyle.SINGLE_CHOICE,
                    pollDuration = PollDuration.ONE_DAY,
                    hideTotals = false,
                ),
            pollInteractions = object : PollInteractions {},
            contentWarningInteractions = object : ContentWarningInteractions {},
            newPostInteractions = NewPostInteractionsNoOp,
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
            newPostUiState = NewPostUiState(
                sendButtonEnabled = true,
                isSendingPost = false,
                visibility = StatusVisibility.Private,
                userHeaderState = UserHeaderState("", "Barack Obama"),
                bottomBarState = BottomBarState(),
            ),
            statusInteractions = object : StatusInteractions {},
            imageStates = listOf(),
            mediaInteractions = object : MediaInteractions {},
            pollUiState = null,
            pollInteractions = object : PollInteractions {},
            contentWarningInteractions = object : ContentWarningInteractions {},
            newPostInteractions = NewPostInteractionsNoOp,
        )
    }
}

@Preview
@Composable
private fun EditPostScreenPreview() {
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
                editStatusId = "statusId"
            ),
            newPostUiState = NewPostUiState(
                sendButtonEnabled = true,
                isSendingPost = false,
                visibility = StatusVisibility.Private,
                userHeaderState = UserHeaderState("", "Barack Obama"),
                bottomBarState = BottomBarState(),
            ),
            statusInteractions = object : StatusInteractions {},
            imageStates = listOf(),
            mediaInteractions = object : MediaInteractions {},
            pollUiState = null,
            pollInteractions = object : PollInteractions {},
            contentWarningInteractions = object : ContentWarningInteractions {},
            newPostInteractions = NewPostInteractionsNoOp,
        )
    }
}