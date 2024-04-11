package social.firefly.core.designsystem.icon

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
import social.firefly.core.designsystem.R

/**
 * Other than aliases, these names should correspond to the icons in the
 * [phosphor library](https://phosphoricons.com/).
 * TODO@release update material icons with phosphor icons
 */
object FfIcons {
    @Composable
    fun at() = painterResource(id = R.drawable.at)

    @Composable
    fun bell() = painterResource(id = R.drawable.bell)

    @Composable
    fun bellFill() = painterResource(id = R.drawable.bell_fill)

    @Composable
    fun bookmark() = painterResource(R.drawable.bookmark_simple)

    @Composable
    fun bookmarkFill() = painterResource(R.drawable.bookmark_simple_fill)

    @Composable
    fun boost() = repeat()

    @Composable
    fun caretDown() = painterResource(id = R.drawable.caret_down)

    @Composable
    fun caretRight() = painterResource(id = R.drawable.caret_right)

    @Composable
    fun chartBar() = painterResource(id = R.drawable.chart_bar)

    @Composable
    fun chatBubbles() = painterResource(id = R.drawable.chat_bubbles)

    @Composable
    fun chatBubblesFilled() = painterResource(id = R.drawable.chat_bubbles_filled)

    @Composable
    fun compass() = painterResource(R.drawable.compass)

    @Composable
    fun compassFill() = painterResource(R.drawable.compass_fill)

    @Composable
    fun connect() = painterResource(id = R.drawable.connect)

    @Composable
    fun downloadSimple() = painterResource(id = R.drawable.download_simple)

    @Composable
    fun following() = painterResource(id = R.drawable.following)

    @Composable
    fun gear() = painterResource(id = R.drawable.gear)

    @Composable
    fun globeHemisphereWest() = painterResource(id = R.drawable.globe_hemisphere_west)

    @Composable
    fun hash() = painterResource(id = R.drawable.hash)

    @Composable
    fun heart() = painterResource(id = R.drawable.heart)

    @Composable
    fun heartFilled() = painterResource(id = R.drawable.heart_filled)

    @Composable
    fun house() = painterResource(R.drawable.house)

    @Composable
    fun houseFill() = painterResource(R.drawable.house_fill)

    @Composable
    fun identificationCard() = painterResource(id = R.drawable.identification_card)

    @Composable
    fun image() = painterResource(id = R.drawable.image)

    @Composable
    fun imagePlus() = painterResource(id = R.drawable.image_plus)

    @Composable
    fun info() = painterResource(id = R.drawable.info)

    @Composable
    fun list() = painterResource(id = R.drawable.list)

    @Composable
    fun listChecks() = painterResource(id = R.drawable.list_checks)

    @Composable
    fun lock() = painterResource(id = R.drawable.lock)

    @Composable
    fun lockKey() = painterResource(id = R.drawable.lock_key)

    @Composable
    fun magnifyingGlass() = painterResource(id = R.drawable.magnifying_glass)

    @Composable
    fun magnifyingGlassFill() = painterResource(id = R.drawable.magnifying_glass_fill)

    @Composable
    fun monitorPlay() = painterResource(id = R.drawable.monitor_play)

    @Composable
    fun moreVertical() = painterResource(id = R.drawable.more_vertical)

    @Composable
    fun note() = painterResource(id = R.drawable.note)

    @Composable
    fun pause() = painterResource(id = R.drawable.pause)

    @Composable
    fun play() = painterResource(id = R.drawable.play)

    @Composable
    fun plus() = painterResource(id = R.drawable.plus)

    @Composable
    fun prohibit() = painterResource(id = R.drawable.prohibit)

    @Composable
    fun repeat() = painterResource(id = R.drawable.repeat)

    @Composable
    fun robot() = painterResource(id = R.drawable.robot)

    @Composable
    fun share() = painterResource(id = R.drawable.share)

    @Composable
    fun speakerX() = painterResource(id = R.drawable.speaker_x)

    @Composable
    fun trash() = painterResource(id = R.drawable.trash)

    @Composable
    fun userCircle() = painterResource(id = R.drawable.user_circle)

    @Composable
    fun userCircleFill() = painterResource(id = R.drawable.user_circle_fill)

    @Composable
    fun userJoin() = painterResource(id = R.drawable.user_join)

    @Composable
    fun users() = painterResource(id = R.drawable.users)

    @Composable
    fun warning() = painterResource(id = R.drawable.warning)

    @Composable
    fun x() = painterResource(id = R.drawable.x)

    // Material- avoid using these, and instead use the drawable based resources
    @Composable
    fun check() = rememberVectorPainter(image = Icons.Rounded.Check)

    @Composable
    fun backArrow() = rememberVectorPainter(image = Icons.Rounded.ArrowBack)

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
