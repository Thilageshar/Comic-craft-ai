package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ComicButton
import com.example.ui.components.ComicPanelCard
import com.example.ui.components.ComicShadowBox
import com.example.ui.components.EditPanelDialog
import com.example.ui.components.HalftoneBackground
import com.example.ui.theme.ComicBlue
import com.example.ui.theme.ComicInk
import com.example.ui.theme.ComicPaper
import com.example.ui.theme.ComicRed
import com.example.ui.theme.ComicYellow
import com.example.ui.viewmodel.ComicViewModel
import kotlinx.coroutines.launch

@Composable
fun ComicViewerScreen(
    viewModel: ComicViewModel,
    onNavigateToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentComic by viewModel.currentComic.collectAsStateWithLifecycle()
    val activePanelForEdit by viewModel.activePanelForEdit.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var isReaderModeActive by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize().background(Color(0xFFF8FAFC))) {
        HalftoneBackground()

        if (currentComic == null) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                ComicShadowBox(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.White,
                    cornerRadius = 24.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(ComicYellow, CircleShape)
                                .border(3.dp, ComicInk, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = ComicInk,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "NO COMIC CREATED YET",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = ComicInk
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Jump over to the Creator tab, write your premise, and tap Generate to create your first visual story!",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        ComicButton(
                            onClick = onNavigateToCreate,
                            backgroundColor = ComicYellow,
                            contentColor = ComicInk,
                            testTag = "start_creating_button"
                        ) {
                            Text(
                                text = "CREATE A COMIC!",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = ComicInk
                            )
                        }
                    }
                }
            }
        } else {
            val comic = currentComic!!

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Comic Story Header Card
                item {
                    ComicShadowBox(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color.White,
                        cornerRadius = 20.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Tone and Art Style badges
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(ComicYellow, RoundedCornerShape(8.dp))
                                        .border(2.dp, ComicInk, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = comic.tone.uppercase(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = ComicInk
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                                        .border(1.5.dp, ComicInk, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = comic.artStyle,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ComicInk
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = comic.title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = ComicInk,
                                textAlign = TextAlign.Center,
                                letterSpacing = 1.sp
                            )

                            if (comic.synopsis.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "“${comic.synopsis}”",
                                    fontSize = 12.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Action Toolbar Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Reader Mode Button
                                Box(
                                    modifier = Modifier
                                        .background(ComicBlue, RoundedCornerShape(12.dp))
                                        .border(2.dp, ComicInk, RoundedCornerShape(12.dp))
                                        .clickable { isReaderModeActive = true }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                            contentDescription = "Reader Mode",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "READER MODE",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // Share Script Button
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                                        .border(2.dp, ComicInk, RoundedCornerShape(12.dp))
                                        .clickable { viewModel.shareComicScript(context, comic) }
                                        .padding(horizontal = 10.dp, vertical = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = "Share",
                                            tint = ComicInk,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "SHARE",
                                            color = ComicInk,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // Favorite Toggle
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (comic.isFavorite) ComicRed else Color(0xFFF1F5F9),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .border(2.dp, ComicInk, RoundedCornerShape(12.dp))
                                        .clickable { viewModel.toggleFavorite(comic) }
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = if (comic.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        tint = if (comic.isFavorite) Color.White else ComicInk,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Panels
                itemsIndexed(comic.panels) { index, panel ->
                    ComicPanelCard(
                        panel = panel,
                        onRerollImage = { viewModel.rerollPanelImage(index) },
                        onEditPanel = { viewModel.selectPanelForEdit(panel) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Edit Panel Dialog
        activePanelForEdit?.let { panel ->
            EditPanelDialog(
                panel = panel,
                onDismiss = { viewModel.selectPanelForEdit(null) },
                onSave = { updated -> viewModel.saveUpdatedPanel(updated) }
            )
        }

        // Fullscreen Reader Mode Dialog
        if (isReaderModeActive && currentComic != null) {
            val comic = currentComic!!
            val pagerState = rememberPagerState(pageCount = { comic.panels.size })
            val coroutineScope = rememberCoroutineScope()

            Dialog(
                onDismissRequest = { isReaderModeActive = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0F172A))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Top bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = comic.title,
                                color = ComicYellow,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = { isReaderModeActive = false },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close reader",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Horizontal Pager for Panels
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) { page ->
                            val panel = comic.panels[page]
                            ComicPanelCard(
                                panel = panel,
                                modifier = Modifier.padding(horizontal = 8.dp),
                                onRerollImage = { viewModel.rerollPanelImage(page) },
                                onEditPanel = {
                                    isReaderModeActive = false
                                    viewModel.selectPanelForEdit(panel)
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Bottom Navigation Controller
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    if (pagerState.currentPage > 0) {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                        }
                                    }
                                },
                                enabled = pagerState.currentPage > 0,
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(ComicYellow, CircleShape)
                                    .border(2.dp, ComicInk, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Previous panel",
                                    tint = ComicInk
                                )
                            }

                            Text(
                                text = "Panel ${pagerState.currentPage + 1} of ${comic.panels.size}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            IconButton(
                                onClick = {
                                    if (pagerState.currentPage < comic.panels.size - 1) {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }
                                    }
                                },
                                enabled = pagerState.currentPage < comic.panels.size - 1,
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(ComicYellow, CircleShape)
                                    .border(2.dp, ComicInk, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next panel",
                                    tint = ComicInk
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
