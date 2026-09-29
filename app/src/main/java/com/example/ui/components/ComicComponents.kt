package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.ComicPanel
import com.example.ui.theme.ComicBlue
import com.example.ui.theme.ComicInk
import com.example.ui.theme.ComicRed
import com.example.ui.theme.ComicYellow
import com.example.ui.theme.ComicYellowLight
import kotlin.math.cos
import kotlin.math.sin

/**
 * Comic Book Style Box with solid ink shadow
 */
@Composable
fun ComicShadowBox(
    modifier: Modifier = Modifier,
    borderWidth: Dp = 2.5.dp,
    shadowOffset: Dp = 4.dp,
    cornerRadius: Dp = 16.dp,
    backgroundColor: Color = Color.White,
    borderColor: Color = ComicInk,
    shadowColor: Color = ComicInk,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        // Shadow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(shadowColor, RoundedCornerShape(cornerRadius))
        )
        // Main layer
        Box(
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(cornerRadius))
                .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
                .clip(RoundedCornerShape(cornerRadius))
        ) {
            content()
        }
    }
}

/**
 * Interactive Comic Button that depresses into its shadow when pressed
 */
@Composable
fun ComicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ComicRed,
    contentColor: Color = Color.White,
    cornerRadius: Dp = 14.dp,
    testTag: String = "comic_button",
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val offsetAnim by animateFloatAsState(targetValue = if (isPressed) 3f else 0f, label = "button_press")

    Box(
        modifier = modifier
            .testTag(testTag)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
    ) {
        // Shadow base
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 4.dp, y = 4.dp)
                .background(ComicInk, RoundedCornerShape(cornerRadius))
        )
        // Active button
        Box(
            modifier = Modifier
                .offset(x = offsetAnim.dp, y = offsetAnim.dp)
                .background(backgroundColor, RoundedCornerShape(cornerRadius))
                .border(2.5.dp, ComicInk, RoundedCornerShape(cornerRadius))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

/**
 * Comic Speech Bubble with Tail
 */
@Composable
fun SpeechBubble(
    speaker: String,
    dialogue: String,
    modifier: Modifier = Modifier,
    isLeftTail: Boolean = true,
    onEditClick: (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(14.dp))
                .border(2.dp, ComicInk, RoundedCornerShape(14.dp))
                .clickable(enabled = onEditClick != null) { onEditClick?.invoke() }
                .padding(10.dp)
        ) {
            Column {
                if (speaker.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 2.dp)
                    ) {
                        Text(
                            text = speaker.uppercase(),
                            color = ComicRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.SansSerif,
                            letterSpacing = 0.5.sp
                        )
                        if (onEditClick != null) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit dialogue",
                                modifier = Modifier.size(10.dp),
                                tint = Color.Gray
                            )
                        }
                    }
                }
                Text(
                    text = "“$dialogue”",
                    color = ComicInk,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    lineHeight = 18.sp
                )
            }
        }
        // Bubble Tail
        Canvas(
            modifier = Modifier
                .size(width = 24.dp, height = 12.dp)
                .offset(x = if (isLeftTail) 20.dp else 120.dp, y = (-2).dp)
        ) {
            val tailPath = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width / 2, size.height)
                lineTo(size.width, 0f)
                close()
            }
            drawPath(tailPath, color = Color.White)
            drawPath(
                Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width / 2, size.height)
                    lineTo(size.width, 0f)
                },
                color = ComicInk,
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}

/**
 * Narration Banner
 */
@Composable
fun NarrationBox(
    caption: String,
    modifier: Modifier = Modifier,
    onEditClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(ComicYellowLight, RoundedCornerShape(8.dp))
            .border(2.dp, ComicInk, RoundedCornerShape(8.dp))
            .clickable(enabled = onEditClick != null) { onEditClick?.invoke() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Text(
                    text = "NARRATION",
                    color = ComicInk.copy(alpha = 0.6f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                if (onEditClick != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit caption",
                        modifier = Modifier.size(10.dp),
                        tint = ComicInk.copy(alpha = 0.5f)
                    )
                }
            }
            Text(
                text = caption,
                color = ComicInk,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic,
                lineHeight = 16.sp
            )
        }
    }
}

/**
 * Sound Effect (Onomatopoeia) Sticker
 */
@Composable
fun SoundEffectBadge(
    text: String,
    modifier: Modifier = Modifier,
    rotation: Float = -6f,
    onClick: (() -> Unit)? = null
) {
    if (text.isBlank()) return

    Box(
        modifier = modifier
            .rotate(rotation)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
    ) {
        // Action Starburst background
        Canvas(modifier = Modifier.size(76.dp, 36.dp)) {
            val w = size.width
            val h = size.height
            val cx = w / 2
            val cy = h / 2
            val points = 12
            val path = Path()

            for (i in 0 until points * 2) {
                val angle = (i * Math.PI / points).toFloat()
                val radius = if (i % 2 == 0) w / 2 else w / 2.6f
                val x = cx + radius * cos(angle)
                val y = cy + (radius * (h / w)) * sin(angle)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()

            // Shadow
            drawPath(path, color = ComicInk)
            // Foreground
            drawPath(path, color = ComicYellow)
            drawPath(path, color = ComicInk, style = Stroke(width = 2.dp.toPx()))
        }

        Text(
            text = text,
            color = ComicRed,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

/**
 * Comic Panel Card
 */
@Composable
fun ComicPanelCard(
    panel: ComicPanel,
    modifier: Modifier = Modifier,
    onRerollImage: () -> Unit,
    onEditPanel: () -> Unit,
    onPlaySfx: (() -> Unit)? = null
) {
    val context = LocalContext.current

    ComicShadowBox(
        modifier = modifier.fillMaxWidth(),
        borderWidth = 2.5.dp,
        shadowOffset = 4.dp,
        cornerRadius = 16.dp,
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ComicInk)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PANEL #${panel.panelNumber}",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.weight(1f))

                // Reroll image icon button
                IconButton(
                    onClick = onRerollImage,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reroll panel image",
                        tint = ComicYellow,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Edit details icon button
                IconButton(
                    onClick = onEditPanel,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit panel text",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Panel Image Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .background(Color(0xFFE2E8F0))
            ) {
                if (panel.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(panel.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Panel ${panel.panelNumber} illustration",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Stylized Comic Placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF1F5F9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ComicYellow,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Panel Scene Visualization",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Sound Effect Badge positioned at top right of image
                if (panel.soundEffect.isNotBlank()) {
                    SoundEffectBadge(
                        text = panel.soundEffect,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        onClick = onPlaySfx
                    )
                }
            }

            // Panel Content (Narration + Dialogue)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFFBEB).copy(alpha = 0.5f))
                    .padding(12.dp)
            ) {
                if (panel.caption.isNotBlank()) {
                    NarrationBox(
                        caption = panel.caption,
                        onEditClick = onEditPanel
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (panel.dialogue.isNotBlank()) {
                    SpeechBubble(
                        speaker = panel.speaker,
                        dialogue = panel.dialogue,
                        onEditClick = onEditPanel
                    )
                }
            }
        }
    }
}

/**
 * Halftone Dot Background Pattern
 */
@Composable
fun HalftoneBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val spacing = 20.dp.toPx()
        val radius = 1.2.dp.toPx()
        val rows = (size.height / spacing).toInt() + 1
        val cols = (size.width / spacing).toInt() + 1

        for (r in 0..rows) {
            for (c in 0..cols) {
                drawCircle(
                    color = ComicInk.copy(alpha = 0.06f),
                    radius = radius,
                    center = Offset(c * spacing, r * spacing)
                )
            }
        }
    }
}
