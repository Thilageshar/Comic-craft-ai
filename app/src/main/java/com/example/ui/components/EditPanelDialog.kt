package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ComicPanel
import com.example.ui.theme.ComicInk
import com.example.ui.theme.ComicRed
import com.example.ui.theme.ComicYellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditPanelDialog(
    panel: ComicPanel,
    onDismiss: () -> Unit,
    onSave: (updatedPanel: ComicPanel) -> Unit
) {
    var caption by remember { mutableStateOf(panel.caption) }
    var speaker by remember { mutableStateOf(panel.speaker) }
    var dialogue by remember { mutableStateOf(panel.dialogue) }
    var soundEffect by remember { mutableStateOf(panel.soundEffect) }

    val quickSfx = listOf("POW!", "BAM!", "ZAP!", "KABOOM!", "WHOOSH!", "BZZZT!", "GASP!", "CLICK!")

    Dialog(onDismissRequest = onDismiss) {
        ComicShadowBox(
            modifier = Modifier.fillMaxWidth(),
            borderWidth = 3.dp,
            cornerRadius = 20.dp,
            backgroundColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "EDIT PANEL #${panel.panelNumber}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = ComicInk
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Caption
                Text(
                    text = "NARRATION / CAPTION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ComicInk.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = caption,
                    onValueChange = { caption = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ComicInk,
                        unfocusedBorderColor = ComicInk.copy(alpha = 0.4f)
                    ),
                    placeholder = { Text("e.g. Meanwhile at the laboratory...") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Speaker
                Text(
                    text = "SPEAKER NAME",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ComicInk.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = speaker,
                    onValueChange = { speaker = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ComicInk,
                        unfocusedBorderColor = ComicInk.copy(alpha = 0.4f)
                    ),
                    placeholder = { Text("e.g. HERO") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Dialogue
                Text(
                    text = "CHARACTER DIALOGUE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ComicInk.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = dialogue,
                    onValueChange = { dialogue = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ComicInk,
                        unfocusedBorderColor = ComicInk.copy(alpha = 0.4f)
                    ),
                    placeholder = { Text("e.g. Look out! It's reacting!") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Sound Effect
                Text(
                    text = "SOUND EFFECT (SFX)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ComicInk.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = soundEffect,
                    onValueChange = { soundEffect = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ComicInk,
                        unfocusedBorderColor = ComicInk.copy(alpha = 0.4f)
                    ),
                    placeholder = { Text("e.g. POW!") }
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Quick SFX chips
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    quickSfx.forEach { sfx ->
                        Box(
                            modifier = Modifier
                                .background(ComicYellow, RoundedCornerShape(8.dp))
                                .border(1.5.dp, ComicInk, RoundedCornerShape(8.dp))
                                .clickable { soundEffect = sfx }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = sfx,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = ComicInk
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ComicButton(
                        onClick = onDismiss,
                        backgroundColor = Color(0xFFF1F5F9),
                        contentColor = ComicInk
                    ) {
                        Text("CANCEL", fontWeight = FontWeight.Bold, color = ComicInk, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    ComicButton(
                        onClick = {
                            onSave(
                                panel.copy(
                                    caption = caption.trim(),
                                    speaker = speaker.trim(),
                                    dialogue = dialogue.trim(),
                                    soundEffect = soundEffect.trim()
                                )
                            )
                        },
                        backgroundColor = ComicYellow,
                        contentColor = ComicInk
                    ) {
                        Text("SAVE PANEL", fontWeight = FontWeight.Black, color = ComicInk, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
