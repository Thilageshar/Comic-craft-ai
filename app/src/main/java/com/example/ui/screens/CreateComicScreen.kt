package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ComicButton
import com.example.ui.components.ComicShadowBox
import com.example.ui.components.HalftoneBackground
import com.example.ui.theme.ComicBlue
import com.example.ui.theme.ComicInk
import com.example.ui.theme.ComicRed
import com.example.ui.theme.ComicYellow
import com.example.ui.theme.ComicYellowLight
import com.example.ui.viewmodel.ComicViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateComicScreen(
    viewModel: ComicViewModel,
    onNavigateToViewer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val settingsList = listOf(
        "Robotics Lab", "Cyberpunk Metropolis", "Ancient Forest", "Mars Outpost", "Quiet Village", "Underwater Ruins"
    )

    val tonesList = listOf(
        "Adventure", "Funny", "Mystery", "Action", "Emotional", "Horror", "Superhero"
    )

    val artStylesList = listOf(
        "Classic Comic Book", "Anime & Manga", "Saturday Cartoon", "Pixar 3D Animation", "Vibrant Watercolor", "Gritty Graphic Novel"
    )

    Box(modifier = modifier.fillMaxSize().background(Color(0xFFF8FAFC))) {
        HalftoneBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            // Header Hero Banner
            ComicShadowBox(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.White,
                cornerRadius = 20.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(ComicYellow, CircleShape)
                                .border(2.dp, ComicInk, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ComicInk,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "COMIC STORY CREATOR",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = ComicInk,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Turn ideas into dynamic multi-panel visual comic strips",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Form Box
            ComicShadowBox(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.White,
                cornerRadius = 20.dp
            ) {
                Column(modifier = Modifier.padding(18.dp)) {

                    // Step 1: Prompt
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "1. STORY PREMISE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = ComicInk
                        )
                        Text(
                            text = " *",
                            color = ComicRed,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = formState.prompt,
                        onValueChange = { viewModel.updatePrompt(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("story_prompt_input"),
                        shape = RoundedCornerShape(14.dp),
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ComicInk,
                            unfocusedBorderColor = ComicInk.copy(alpha = 0.35f)
                        ),
                        placeholder = {
                            Text(
                                "Describe your story idea... (e.g. A young student discovers a mysterious mini-robot under their college lab desk)",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Preset Ideas
                    Text(
                        text = "⚡ Quick Inspiration Prompts:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ComicBlue
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IdeaChip("Mini-Robot in Lab") {
                            viewModel.useExamplePrompt(
                                prompt = "A young student discovers a glowing miniature robot hidden inside the university robotics cabinet.",
                                charName = "Aiden & Bot-9",
                                setting = "Robotics Lab",
                                tone = "Adventure"
                            )
                        }
                        IdeaChip("Cyber Pizza Courier") {
                            viewModel.useExamplePrompt(
                                prompt = "A futuristic pizza delivery driver must navigate floating cyber-traffic while protecting the secret recipe.",
                                charName = "Kai",
                                setting = "Cyberpunk Metropolis",
                                tone = "Funny"
                            )
                        }
                        IdeaChip("Detective Hamster") {
                            viewModel.useExamplePrompt(
                                prompt = "An unusually observant pet hamster uncovers who really stole the kitchen cookie jar.",
                                charName = "Inspector Pip",
                                setting = "Quiet Village",
                                tone = "Mystery"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Step 2: Character & Setting
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "2. MAIN HERO",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = ComicInk
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = formState.characterName,
                                onValueChange = { viewModel.updateCharacterName(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("character_name_input"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ComicInk,
                                    unfocusedBorderColor = ComicInk.copy(alpha = 0.35f)
                                ),
                                placeholder = { Text("e.g. Leo the Coder", fontSize = 12.sp) }
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "3. SETTING",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = ComicInk
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = formState.setting,
                                onValueChange = { viewModel.updateSetting(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("setting_input"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ComicInk,
                                    unfocusedBorderColor = ComicInk.copy(alpha = 0.35f)
                                ),
                                placeholder = { Text("e.g. College Campus", fontSize = 12.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Setting chips
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        settingsList.forEach { s ->
                            SettingChip(s, isSelected = formState.setting == s) {
                                viewModel.updateSetting(s)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Step 4: Tone
                    Text(
                        text = "4. STORY TONE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = ComicInk
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tonesList.forEach { tone ->
                            ToneChip(tone, isSelected = formState.tone == tone) {
                                viewModel.updateTone(tone)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Step 5: Art Style
                    Text(
                        text = "5. ART STYLE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = ComicInk
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        artStylesList.forEach { style ->
                            ArtStyleChip(style, isSelected = formState.artStyle == style) {
                                viewModel.updateArtStyle(style)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Step 6: Panels Count
                    Text(
                        text = "6. NUMBER OF PANELS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = ComicInk
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(2, 4, 6, 8).forEach { count ->
                            val isSelected = formState.panelCount == count
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isSelected) ComicYellow else Color(0xFFF1F5F9),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .border(2.dp, ComicInk, RoundedCornerShape(12.dp))
                                    .clickable { viewModel.updatePanelCount(count) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$count Panels",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                    color = ComicInk
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Loading State indicator
                    AnimatedVisibility(visible = formState.isGenerating) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ComicYellowLight, RoundedCornerShape(12.dp))
                                .border(2.dp, ComicInk, RoundedCornerShape(12.dp))
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = ComicRed,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = formState.statusMessage.ifBlank { "Crafting your comic storyboard..." },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ComicInk
                            )
                        }
                    }

                    if (!formState.isGenerating) {
                        // Big Comic Generate Button
                        ComicButton(
                            onClick = {
                                if (formState.prompt.isNotBlank()) {
                                    viewModel.generateComic {
                                        onNavigateToViewer()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = ComicRed,
                            testTag = "generate_comic_button"
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "GENERATE COMIC!",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = ComicYellow,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun IdeaChip(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(Color(0xFFEFF6FF), RoundedCornerShape(8.dp))
            .border(1.5.dp, ComicInk.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = ComicInk
        )
    }
}

@Composable
private fun SettingChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(if (isSelected) ComicYellow else Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
            .border(1.5.dp, if (isSelected) ComicInk else ComicInk.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = ComicInk
        )
    }
}

@Composable
private fun ToneChip(tone: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(if (isSelected) ComicYellow else Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
            .border(2.dp, if (isSelected) ComicInk else ComicInk.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = tone,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
            color = ComicInk
        )
    }
}

@Composable
private fun ArtStyleChip(style: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(if (isSelected) ComicYellow else Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
            .border(2.dp, if (isSelected) ComicInk else ComicInk.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = style,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
            color = ComicInk
        )
    }
}
