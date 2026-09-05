package com.music.bitchord.ui.components

import com.music.bitchord.R

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.music.bitchord.data.model.ROW_ART_PX
import com.music.bitchord.data.model.Song
import com.music.bitchord.data.model.artworkAt
import com.music.bitchord.data.settings.AppSettings
import com.music.bitchord.ui.components.thumbnailBorder
import com.music.bitchord.ui.haptics.Haptic
import com.music.bitchord.ui.haptics.rememberHaptics
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

/**
 * The transport buttons' touch target. Material's default 48dp is what a bar
 * this slim is really made of, so it sets the height on its own.
 */
private val GLYPH_SLOT = 40.dp

/**
 * The play and skip glyphs themselves.
 */
private val GLYPH_SIZE = 30.dp

/** The spinner that stands in for the play glyph, kept in proportion to it. */
private val SPINNER_SIZE = 24.dp

/**
 * The gap between the two transport controls.
 */
private val TRANSPORT_GAP = 2.dp

/**
 * Vertical padding, which with the 40dp artwork sets the bar's height at 56dp
 * and so its pill radius at 28.
 */
private val ROW_PADDING_VERTICAL = 8.dp

/**
 * Horizontal padding, deliberately larger than the vertical.
 *
 * A pill's ends are semicircles, so the edge nearest the artwork is not the
 * one beside it but the one curving away above and below it. At the artwork's
 * top corner that edge has already come 8.4dp in from the left — level with
 * where square corners would have put the whole side. Padding the ends by the
 * vertical figure would leave the artwork touching the curve; 12 clears it
 * with room, and reads as centred rather than jammed into the round.
 */
private val ROW_PADDING_HORIZONTAL = 16.dp

/**
 * The artwork's corner, on the 8dp every other thumbnail in the app carries.
 *
 * It used to be 7, picked so the bar's corner could sit concentric with it.
 * A pill has no corner to be concentric with — its radius is whatever half the
 * height happens to be — so that constraint is gone and the artwork can go
 * back to matching [SongRow].
 */
private val ART_CORNER = 6.dp

/** Docked mini player that sits directly above the bottom navigation bar. */
@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun MiniPlayer(
    song: Song,
    isPlaying: Boolean,
    isLoading: Boolean,
    hazeState: HazeState,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onExpand: () -> Unit,
    positionMs: Long = 0L,
    durationMs: Long = 0L,
    modifier: Modifier = Modifier,
) {
    val reduceDynamicBlur by AppSettings.reduceDynamicBlur.collectAsStateWithLifecycle()
    val haptics = rememberHaptics()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (reduceDynamicBlur) {
                    Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                } else {
                    Modifier.optimizedHazeEffect(
                        state = hazeState,
                        style = HazeMaterials.thin(MaterialTheme.colorScheme.surfaceVariant),
                    )
                },
            )
            .clickable(onClick = onExpand),
    ) {
        // Top hairline divider
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f),
            modifier = Modifier.align(Alignment.TopCenter),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ROW_PADDING_HORIZONTAL,
                    vertical = ROW_PADDING_VERTICAL,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = song.artworkAt(ROW_ART_PX),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(ART_CORNER))
                    .thumbnailBorder(RoundedCornerShape(ART_CORNER))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                ExplicitSongTitle(
                    song = song,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (isLoading) {
                Box(Modifier.size(GLYPH_SLOT), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onBackground,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(SPINNER_SIZE),
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        haptics.play(if (isPlaying) Haptic.Pause else Haptic.Resume)
                        onPlayPause()
                    },
                    modifier = Modifier.size(GLYPH_SLOT),
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = stringResource(if (isPlaying) R.string.pause else R.string.play),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(GLYPH_SIZE),
                    )
                }
            }
            Spacer(Modifier.width(TRANSPORT_GAP))
            IconButton(
                onClick = {
                    haptics.play(Haptic.SkipNext)
                    onNext()
                },
                modifier = Modifier.size(GLYPH_SLOT),
            ) {
                Icon(
                    Icons.Rounded.FastForward,
                    contentDescription = stringResource(R.string.widget_next),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(GLYPH_SIZE),
                )
            }
        }
    }
}
