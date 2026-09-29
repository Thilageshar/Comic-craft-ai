package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.ComicViewerScreen
import com.example.ui.screens.CreateComicScreen
import com.example.ui.screens.MyComicsScreen
import com.example.ui.theme.ComicCraftTheme
import com.example.ui.theme.ComicInk
import com.example.ui.theme.ComicRed
import com.example.ui.theme.ComicYellow
import com.example.ui.theme.ComicYellowLight
import com.example.ui.viewmodel.ComicViewModel

enum class ComicAppTab(val title: String, val icon: ImageVector) {
    CREATE("Create", Icons.Default.Palette),
    VIEWER("Viewer", Icons.AutoMirrored.Filled.MenuBook),
    LIBRARY("Library", Icons.Default.CollectionsBookmark)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComicCraftTheme {
                val viewModel: ComicViewModel = viewModel()
                ComicCraftApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ComicCraftApp(viewModel: ComicViewModel) {
    var selectedTab by remember { mutableStateOf(ComicAppTab.CREATE) }
    var showInfoDialog by remember { mutableStateOf(false) }

    // BackHandler to navigate back to CREATE screen if on secondary tab
    BackHandler(enabled = selectedTab != ComicAppTab.CREATE) {
        selectedTab = ComicAppTab.CREATE
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ComicTopBar(onInfoClick = { showInfoDialog = true })
        },
        bottomBar = {
            ComicBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                ComicAppTab.CREATE -> {
                    CreateComicScreen(
                        viewModel = viewModel,
                        onNavigateToViewer = { selectedTab = ComicAppTab.VIEWER }
                    )
                }
                ComicAppTab.VIEWER -> {
                    ComicViewerScreen(
                        viewModel = viewModel,
                        onNavigateToCreate = { selectedTab = ComicAppTab.CREATE }
                    )
                }
                ComicAppTab.LIBRARY -> {
                    MyComicsScreen(
                        viewModel = viewModel,
                        onComicSelected = { selectedTab = ComicAppTab.VIEWER },
                        onNavigateToCreate = { selectedTab = ComicAppTab.CREATE }
                    )
                }
            }
        }
    }

    // Info / About Dialog
    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(ComicYellow, CircleShape)
                            .border(1.5.dp, ComicInk, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = ComicInk,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ABOUT COMICCRAFT",
                        fontWeight = FontWeight.Black,
                        color = ComicInk
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "ComicCraft transforms your storytelling ideas into panel-by-panel comic strips complete with dialogues, captions, and expressive visual scenes.",
                        fontSize = 13.sp,
                        color = ComicInk
                    )
                    Text(
                        text = "⚡ Powered by Gemini AI (model: gemini-3.5-flash) with seamless offline creative fallback.\n💾 Comics are saved locally in Room SQLite database.\n🎨 Supports 6+ art styles and customizable speech bubbles.",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text("AWESOME", fontWeight = FontWeight.Black, color = ComicRed)
                }
            }
        )
    }
}

@Composable
fun ComicTopBar(onInfoClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.statusBars)
            .border(width = 0.dp, color = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 2.dp, color = ComicInk)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo and Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(ComicYellow, RoundedCornerShape(10.dp))
                        .border(2.dp, ComicInk, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Logo",
                        tint = ComicInk,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "COMICCRAFT",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = ComicInk,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(ComicRed, RoundedCornerShape(6.dp))
                            .border(1.dp, ComicInk, RoundedCornerShape(6.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "AI",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }

            // Info button
            IconButton(
                onClick = onInfoClick,
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
                    .border(1.5.dp, ComicInk, RoundedCornerShape(10.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "About ComicCraft",
                    tint = ComicInk,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun ComicBottomBar(
    selectedTab: ComicAppTab,
    onTabSelected: (ComicAppTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        NavigationBar(
            containerColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, ComicInk)
                .height(64.dp)
        ) {
            ComicAppTab.entries.forEach { tab ->
                val isSelected = selectedTab == tab
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            tint = if (isSelected) ComicRed else ComicInk
                        )
                    },
                    label = {
                        Text(
                            text = tab.title.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = if (isSelected) ComicRed else ComicInk
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ComicRed,
                        unselectedIconColor = ComicInk,
                        selectedTextColor = ComicRed,
                        unselectedTextColor = ComicInk,
                        indicatorColor = ComicYellowLight
                    ),
                    modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
                )
            }
        }
    }
}
