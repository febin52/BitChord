package com.music.bitchord.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.music.bitchord.data.settings.AppSettings
import com.music.bitchord.ui.haptics.Haptic
import com.music.bitchord.ui.haptics.rememberHaptics
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

data class BottomTab(
    val label: String,
    @DrawableRes val iconRes: Int,
    @DrawableRes val selectedIconRes: Int = iconRes,
)

internal val PILL_INSET = 6.dp
internal val TAB_VERTICAL_PADDING = 9.dp
internal val TAB_ICON_LABEL_GAP = 2.dp

/**
 * Apple Music docked bottom navigation bar.
 * Runs edge-to-edge, flush at the bottom with a hairline top divider.
 */
@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun FloatingBottomBar(
    tabs: List<BottomTab>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
) {
    val container = MaterialTheme.colorScheme.surface
    val reduceDynamicBlur by AppSettings.reduceDynamicBlur.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (reduceDynamicBlur) {
                    Modifier.background(container)
                } else {
                    Modifier.optimizedHazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular(container),
                    )
                },
            ),
    ) {
        // Top hairline divider
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            tabs.forEachIndexed { index, tab ->
                BottomBarItem(
                    tab = tab,
                    selected = index == selectedIndex,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    tab: BottomTab,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptics = rememberHaptics()
    val tint by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            Color(0xFF8E8E93)
        },
        animationSpec = tween(180),
        label = "tabTint",
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                if (!selected) haptics.play(Haptic.Select)
                onClick()
            }
            .padding(vertical = 4.dp),
    ) {
        Icon(
            painter = painterResource(if (selected) tab.selectedIconRes else tab.iconRes),
            contentDescription = tab.label,
            tint = tint,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = tab.label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.5.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            ),
            color = tint,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
