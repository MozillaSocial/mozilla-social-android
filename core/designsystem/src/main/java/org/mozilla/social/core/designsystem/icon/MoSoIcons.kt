package org.mozilla.social.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.Poll
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.VolumeMute
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.mozilla.social.core.designsystem.R

/**
 * Other than aliases, these names should correspond to the icons in the
 * [phosphor library](https://phosphoricons.com/).
 * TODO@release update material icons with phosphor icons
 */
object MoSoIcons {

    @Composable
    fun bookmark() = painterResource(R.drawable.bookmark_simple)

    @Composable
    fun robot() = painterResource(id = R.drawable.robot)

    @Composable
    fun caret_down() = painterResource(id = R.drawable.caret_down)

    @Composable
    fun chartBar() = painterResource(id = R.drawable.chart_bar)

    @Composable
    fun chatBubbles() = painterResource(id = R.drawable.chat_bubbles)

    @Composable
    fun chatBubblesFilled() = painterResource(id = R.drawable.chat_bubbles_filled)

    @Composable
    fun compass() = painterResource(R.drawable.compass)

    @Composable
    fun heart() = painterResource(id = R.drawable.heart)

    @Composable
    fun heartFilled() = painterResource(id = R.drawable.heart_filled)

    @Composable
    fun house() = painterResource(R.drawable.house)

    @Composable
    fun image() = painterResource(id = R.drawable.image)

    @Composable
    fun imagePlus() = painterResource(id = R.drawable.image_plus)

    @Composable
    fun list() = painterResource(id = R.drawable.list)

    @Composable
    fun monitorPlay() = painterResource(id = R.drawable.monitor_play)
    @Composable
    fun moreVertical() = painterResource(id = R.drawable.more_vertical)
    @Composable
    fun plus() = painterResource(id = R.drawable.plus)

    @Composable
    fun boost() = repeat()

    @Composable
    fun repeat() = painterResource(id = R.drawable.repeat)

    @Composable
    fun share() = painterResource(id = R.drawable.share)

    @Composable
    fun userCircle() = painterResource(id = R.drawable.user_circle)

    @Composable
    fun userJoin() = painterResource(id = R.drawable.user_join)

    @Composable
    fun warning() = painterResource(id = R.drawable.warning)

    @Composable
    fun x() = painterResource(id = R.drawable.x)

    @Composable
    fun lock() = painterResource(id = R.drawable.lock)

    // Material- avoid using these, and instead use the drawable based resources
    @Composable
    fun check() = rememberVectorPainter(image = Icons.Rounded.Check)

    @Composable
    fun backArrow() = rememberVectorPainter(image = Icons.Rounded.ArrowBack)

    @Composable
    fun info() = rememberVectorPainter(image = Icons.Rounded.Info)

    @Composable
    fun chevronRight() = rememberVectorPainter(image = Icons.Rounded.ChevronRight)

    @Composable
    fun caret() = rememberVectorPainter(image = Icons.Rounded.KeyboardArrowUp)

    @Composable
    fun bookmarkBorder() = rememberVectorPainter(image = Icons.Rounded.BookmarkBorder)

    @Composable
    fun public() = rememberVectorPainter(image = Icons.Rounded.Public)

    @Composable
    fun lockOpen() = rememberVectorPainter(image = Icons.Rounded.LockOpen)

    @Composable
    fun materialLock() = rememberVectorPainter(image = Icons.Rounded.Lock)

    @Composable
    fun message() = rememberVectorPainter(image = Icons.Rounded.Message)

    @Composable
    fun arrowDropDown() = rememberVectorPainter(image = Icons.Rounded.ArrowDropDown)

    @Composable
    fun volumeMute() = rememberVectorPainter(image = Icons.Rounded.VolumeMute)

    @Composable
    fun volumeUp() = rememberVectorPainter(image = Icons.Rounded.VolumeUp)

    @Composable
    fun send() = rememberVectorPainter(image = Icons.Rounded.Send)

    @Composable
    fun addPhotoAlternate() = rememberVectorPainter(image = Icons.Rounded.AddPhotoAlternate)

    @Composable
    fun poll() = rememberVectorPainter(image = Icons.Rounded.Poll)

    @Composable
    fun delete() = rememberVectorPainter(image = Icons.Rounded.Delete)

    @Composable
    fun deleteOutline() = rememberVectorPainter(image = Icons.Rounded.DeleteOutline)

    @Composable
    fun addCircleOutline() = rememberVectorPainter(image = Icons.Rounded.AddCircleOutline)


    // This isn't part of the design system
    object Sizes {
        val small = 16.dp // Used for smaller icons
        val normal = 24.dp // used for most icons, and the navigation icons
    }
}
