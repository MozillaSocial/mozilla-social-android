/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package social.firefly.core.ui.common.tabs

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import social.firefly.core.designsystem.theme.FfTheme
import social.firefly.core.ui.common.divider.FfDivider
import social.firefly.core.ui.common.tabs.TabRowDefaults.tabIndicatorOffset
import kotlin.math.min

/**
 * Based on [ScrollableTabRow]
 * Modified it to dynamically calculate the minimum tab width so that if all tabs
 * can fit inside the parent's width, they will take up the entire space.
 */
@Composable
fun FfTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = FfTheme.colors.layer1,
    contentColor: Color = TabRowDefaults.contentColor,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = @Composable { tabPositions ->
        if (selectedTabIndex < tabPositions.size) {
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = FfTheme.colors.borderAccent,
            )
        }
    },
    divider: @Composable () -> Unit = @Composable {
        FfDivider()
    },
    scrollIndicator: @Composable () -> Unit = @Composable {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            FfTheme.colors.layer1,
                        )
                    )
                )
        )
    },
    tabs: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor
    ) {
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()
        val scrollableTabData = remember(scrollState, coroutineScope) {
            ScrollableTabData(
                scrollState = scrollState,
                coroutineScope = coroutineScope
            )
        }

        var maxWidth by remember { mutableIntStateOf(0) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    maxWidth = it.width
                }
        ) {
            // don't compose until we have the max width
            if (maxWidth == 0) return@Box
            SubcomposeLayout(
                Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.CenterStart)
                    .horizontalScroll(scrollState)
                    .selectableGroup()
                    .clipToBounds()
            ) { constraints ->
                val tabMeasurables = subcompose(TabSlots.Tabs, tabs)

                val calculatedTabMinWidth = calculateTabMinWidth(
                    tabWidthValuesPx = tabMeasurables
                        .map { it.maxIntrinsicWidth(Constraints.Infinity) },
                    containerWidthPx = maxWidth
                )

                val isScrollEnabled = calculatedTabMinWidth == null

                val minTabWidth =
                    calculatedTabMinWidth ?: ScrollableTabRowMinimumTabWidth.roundToPx()

                val layoutHeight = tabMeasurables.fold(initial = 0) { curr, measurable ->
                    maxOf(curr, measurable.maxIntrinsicHeight(Constraints.Infinity))
                }

                val tabConstraints = constraints.copy(
                    minWidth = minTabWidth,
                    minHeight = layoutHeight,
                    maxHeight = layoutHeight,
                )
                val tabPlaceables = tabMeasurables
                    .map { it.measure(tabConstraints) }

                val layoutWidth = tabPlaceables.fold(initial = 0) { curr, measurable ->
                    curr + measurable.width
                }

                // Position the children.
                layout(layoutWidth, layoutHeight) {
                    // Place the tabs
                    val tabPositions = mutableListOf<TabPosition>()
                    var left = 0
                    tabPlaceables.forEach {
                        it.placeRelative(left, 0)
                        tabPositions.add(TabPosition(left = left.toDp(), width = it.width.toDp()))
                        left += it.width
                    }

                    // The divider is measured with its own height, and width equal to the total width
                    // of the tab row, and then placed on top of the tabs.
                    subcompose(TabSlots.Divider, divider).forEach {
                        val placeable = it.measure(
                            constraints.copy(
                                minHeight = 0,
                                minWidth = layoutWidth,
                                maxWidth = layoutWidth
                            )
                        )
                        placeable.placeRelative(0, layoutHeight - placeable.height)
                    }

                    // The indicator container is measured to fill the entire space occupied by the tab
                    // row, and then placed on top of the divider.
                    subcompose(TabSlots.Indicator) {
                        indicator(tabPositions)
                    }.forEach {
                        it.measure(Constraints.fixed(layoutWidth, layoutHeight)).placeRelative(0, 0)
                    }

                    // Add a scroll indicator to the end of the visible end of the tab row
                    // if the tab row is scrollable
                    if (isScrollEnabled) {
                        subcompose(TabSlots.Fade, scrollIndicator).forEach {
                            val scrollRemaining = scrollState.maxValue - scrollState.value
                            val fadeStart = min(FadeWidth.roundToPx(), scrollRemaining)
                            val placeable = it.measure(Constraints.fixed(fadeStart, layoutHeight))
                            placeable.placeRelative(
                                maxWidth + scrollState.value - fadeStart,
                                0,
                            )
                        }
                    }

                    scrollableTabData.onLaidOut(
                        density = this@SubcomposeLayout,
                        edgeOffset = 0,
                        tabPositions = tabPositions,
                        selectedTab = selectedTabIndex
                    )
                }
            }
        }
    }
}

/**
 * Finds the ideal minimum tab width.
 *
 * @param tabWidthValuesPx a list of the widths of all the tabs
 * @param containerWidthPx the width of the container
 * @return the ideal minimum tab width. If all tabs cannot fit into the container,
 * null will be returned
 */
private fun calculateTabMinWidth(
    tabWidthValuesPx: List<Int>,
    containerWidthPx: Int,
): Int? = if (tabWidthValuesPx.sum() < containerWidthPx) {
    val equalWidth = containerWidthPx / tabWidthValuesPx.size
    val totalOfValuesOver = tabWidthValuesPx.filter { it >= equalWidth }.sum()
    val valuesUnder = tabWidthValuesPx.filter { it < equalWidth }
    val numberOfValuesUnder = valuesUnder.size
    val remainingWidth = containerWidthPx - totalOfValuesOver
    val averageRemainingWidth = remainingWidth / numberOfValuesUnder
    if (valuesUnder.max() > averageRemainingWidth) {
        calculateTabMinWidth(
            tabWidthValuesPx = valuesUnder,
            containerWidthPx = remainingWidth,
        )
    } else {
        averageRemainingWidth
    }
} else {
    null
}

/**
 * Data class that contains information about a tab's position on screen, used for calculating
 * where to place the indicator that shows which tab is selected.
 *
 * @property left the left edge's x position from the start of the [TabRow]
 * @property right the right edge's x position from the start of the [TabRow]
 * @property width the width of this tab
 */
@Immutable
class TabPosition internal constructor(val left: Dp, val width: Dp) {
    val right: Dp get() = left + width

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabPosition) return false

        if (left != other.left) return false
        if (width != other.width) return false

        return true
    }

    override fun hashCode(): Int {
        var result = left.hashCode()
        result = 31 * result + width.hashCode()
        return result
    }

    override fun toString(): String {
        return "TabPosition(left=$left, right=$right, width=$width)"
    }
}

/**
 * Contains default implementations and values used for TabRow.
 */
object TabRowDefaults {
    /** Default content color of a tab row. */
    val contentColor: Color
        @Composable get() =
            FfTheme.colors.layer1

    /**
     * Default indicator, which will be positioned at the bottom of the [TabRow], on top of the
     * divider.
     *
     * @param modifier modifier for the indicator's layout
     * @param height height of the indicator
     * @param color color of the indicator
     */
    @Composable
    fun Indicator(
        modifier: Modifier = Modifier,
        height: Dp = 3.dp,
        color: Color = FfTheme.colors.layer1
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .height(height)
                .background(color = color)
        )
    }

    /**
     * [Modifier] that takes up all the available width inside the [TabRow], and then animates
     * the offset of the indicator it is applied to, depending on the [currentTabPosition].
     *
     * @param currentTabPosition [TabPosition] of the currently selected tab. This is used to
     * calculate the offset of the indicator this modifier is applied to, as well as its width.
     */
    fun Modifier.tabIndicatorOffset(
        currentTabPosition: TabPosition
    ): Modifier = composed(
        inspectorInfo = debugInspectorInfo {
            name = "tabIndicatorOffset"
            value = currentTabPosition
        }
    ) {
        val currentTabWidth by animateDpAsState(
            targetValue = currentTabPosition.width,
            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
        )
        val indicatorOffset by animateDpAsState(
            targetValue = currentTabPosition.left,
            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
        )
        fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = indicatorOffset)
            .width(currentTabWidth)
    }
}

private enum class TabSlots {
    Tabs,
    Divider,
    Indicator,
    Fade,
}

/**
 * Class holding onto state needed for [ScrollableTabRow]
 */
private class ScrollableTabData(
    private val scrollState: ScrollState,
    private val coroutineScope: CoroutineScope
) {
    private var selectedTab: Int? = null

    fun onLaidOut(
        density: Density,
        edgeOffset: Int,
        tabPositions: List<TabPosition>,
        selectedTab: Int
    ) {
        // Animate if the new tab is different from the old tab, or this is called for the first
        // time (i.e selectedTab is `null`).
        if (this.selectedTab != selectedTab) {
            this.selectedTab = selectedTab
            tabPositions.getOrNull(selectedTab)?.let {
                // Scrolls to the tab with [tabPosition], trying to place it in the center of the
                // screen or as close to the center as possible.
                val calculatedOffset = it.calculateTabOffset(density, edgeOffset, tabPositions)
                if (scrollState.value != calculatedOffset) {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(
                            calculatedOffset,
                            animationSpec = ScrollableTabRowScrollSpec
                        )
                    }
                }
            }
        }
    }

    /**
     * @return the offset required to horizontally center the tab inside this TabRow.
     * If the tab is at the start / end, and there is not enough space to fully centre the tab, this
     * will just clamp to the min / max position given the max width.
     */
    private fun TabPosition.calculateTabOffset(
        density: Density,
        edgeOffset: Int,
        tabPositions: List<TabPosition>
    ): Int = with(density) {
        val totalTabRowWidth = tabPositions.last().right.roundToPx() + edgeOffset
        val visibleWidth = totalTabRowWidth - scrollState.maxValue
        val tabOffset = left.roundToPx()
        val scrollerCenter = visibleWidth / 2
        val tabWidth = width.roundToPx()
        val centeredTabOffset = tabOffset - (scrollerCenter - tabWidth / 2)
        // How much space we have to scroll. If the visible width is <= to the total width, then
        // we have no space to scroll as everything is always visible.
        val availableSpace = (totalTabRowWidth - visibleWidth).coerceAtLeast(0)
        return centeredTabOffset.coerceIn(0, availableSpace)
    }
}

private val ScrollableTabRowMinimumTabWidth = 0.dp

private val FadeWidth = 120.dp

/**
 * [AnimationSpec] used when scrolling to a tab that is not fully visible.
 */
private val ScrollableTabRowScrollSpec: AnimationSpec<Float> = tween(
    durationMillis = 250,
    easing = FastOutSlowInEasing
)

@Preview
@Composable
private fun TabRowPreview() {
    FfTheme {
        FfTabRow(selectedTabIndex = 0) {
            FfTab(selected = true, onClick = { /*TODO*/ }) {
                Text(text = "test")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test2")
            }
        }
    }
}

@Preview
@Composable
private fun TabRowPreview2() {
    FfTheme {
        FfTabRow(selectedTabIndex = 0) {
            FfTab(selected = true, onClick = { /*TODO*/ }) {
                Text(text = "test")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test2")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test and stuff 2")
            }
        }
    }
}

@Preview
@Composable
private fun TabRowPreview3() {
    FfTheme {
        FfTabRow(selectedTabIndex = 0) {
            FfTab(selected = true, onClick = { /*TODO*/ }) {
                Text(text = "test")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test2")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test and some cool stuff that I like a lot")
            }
        }
    }
}

@Preview
@Composable
private fun TabRowPreview4() {
    FfTheme {
        FfTabRow(selectedTabIndex = 0) {
            FfTab(selected = true, onClick = { /*TODO*/ }) {
                Text(text = "test")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test2")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test and some cool stuff")
            }
        }
    }
}

@Preview
@Composable
private fun TabRowPreview5() {
    FfTheme {
        FfTabRow(selectedTabIndex = 0) {
            FfTab(selected = true, onClick = { /*TODO*/ }) {
                Text(text = "test")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test and stuff 2")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test and some cool stuff")
            }
        }
    }
}

@Preview
@Composable
private fun TabRowPreview6() {
    FfTheme {
        FfTabRow(selectedTabIndex = 0) {
            FfTab(selected = true, onClick = { /*TODO*/ }) {
                Text(text = "test")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test and some cool stuff its neat")
            }
        }
    }
}

@Preview
@Composable
private fun TabRowPreview7() {
    FfTheme {
        FfTabRow(selectedTabIndex = 0) {
            FfTab(selected = true, onClick = { /*TODO*/ }) {
                Text(text = "test")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test 2333")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test and some cool")
            }
        }
    }
}

@Preview
@Composable
private fun TabRowPreview8() {
    FfTheme {
        FfTabRow(selectedTabIndex = 0) {
            FfTab(selected = true, onClick = { /*TODO*/ }) {
                Text(text = "test is super cool")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "full gradient!")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test 2333")
            }
            FfTab(selected = false, onClick = { /*TODO*/ }) {
                Text(text = "test and some cool fun")
            }
        }
    }
}