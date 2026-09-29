package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.ComicStory
import com.example.ui.components.ComicButton
import com.example.ui.components.ComicShadowBox
import com.example.ui.components.HalftoneBackground
import com.example.ui.theme.ComicInk
import com.example.ui.theme.ComicRed
import com.example.ui.theme.ComicYellow
import com.example.ui.viewmodel.ComicViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyComicsScreen(
    viewModel: ComicViewModel,
    onComicSelected: (ComicStory) -> Unit,
    onNavigateToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allComics by viewModel.savedComics.collectAsStateWithLifecycle()
    val favoriteComics by viewModel.favoriteComics.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var showOnlyFavorites by remember { mutableStateOf(false) }
    var comicToDelete by remember { mutableStateOf<ComicStory?>(null) }

    val activeList = if (showOnlyFavorites) favoriteComics else allComics
    val filteredList = remember(activeList, searchQuery) {
        if (searchQuery.isBlank()) {
            activeList
        } else {
            activeList.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.synopsis.contains(searchQuery, ignoreCase = true) ||
                it.characterName.contains(searchQuery, ignoreCase = true) ||
                it.setting.contains(searchQuery, ignoreCase = true) ||
                it.tone.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize().background(Color(0xFFF8FAFC))) {
        HalftoneBackground()

        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "MY COMIC LIBRARY",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = ComicInk,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "${allComics.size} stories crafted and saved locally",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Search Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth().testTag("comic_search_input"),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    placeholder = { Text("Search by title, character, or setting...", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = ComicInk
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ComicInk,
                        unfocusedBorderColor = ComicInk.copy(alpha = 0.35f)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Tabs: All vs Favorites
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TabPill(
                        label = "All Comics (${allComics.size})",
                        isSelected = !showOnlyFavorites
                    ) {
                        showOnlyFavorites = false
                    }
                    TabPill(
                        label = "Favorites (${favoriteComics.size})",
                        isSelected = showOnlyFavorites
                    ) {
                        showOnlyFavorites = true
                    }
                }
            }

            // List of Comics
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(ComicYellow, CircleShape)
                                .border(2.5.dp, ComicInk, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderSpecial,
                                contentDescription = null,
                                tint = ComicInk,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (showOnlyFavorites) "No Favorite Comics Yet" else "No Comics In Library",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = ComicInk
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (showOnlyFavorites) "Tap the heart on any comic to add it here!" else "Generate your first comic strip using the Creator tab!",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ComicButton(
                            onClick = onNavigateToCreate,
                            backgroundColor = ComicYellow,
                            contentColor = ComicInk
                        ) {
                            Text(
                                text = "CREATE NOW",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = ComicInk
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filteredList, key = { it.id }) { story ->
                        ComicItemCard(
                            story = story,
                            onClick = {
                                viewModel.loadComic(story)
                                onComicSelected(story)
                            },
                            onToggleFavorite = { viewModel.toggleFavorite(story) },
                            onDelete = { comicToDelete = story }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        comicToDelete?.let { story ->
            AlertDialog(
                onDismissRequest = { comicToDelete = null },
                title = {
                    Text(
                        text = "DELETE COMIC?",
                        fontWeight = FontWeight.Black,
                        color = ComicInk
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete \"${story.title}\"? This action cannot be undone.",
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteComic(story)
                            comicToDelete = null
                        }
                    ) {
                        Text("DELETE", color = ComicRed, fontWeight = FontWeight.Black)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { comicToDelete = null }) {
                        Text("CANCEL", color = ComicInk, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Composable
private fun TabPill(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(if (isSelected) ComicYellow else Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
            .border(2.dp, if (isSelected) ComicInk else ComicInk.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
            color = ComicInk
        )
    }
}

@Composable
private fun ComicItemCard(
    story: ComicStory,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val coverUrl = story.panels.firstOrNull()?.imageUrl ?: ""
    val dateStr = remember(story.createdAt) {
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(story.createdAt))
    }

    ComicShadowBox(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        cornerRadius = 16.dp,
        borderWidth = 2.dp,
        shadowOffset = 3.dp,
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE2E8F0))
                    .border(1.5.dp, ComicInk, RoundedCornerShape(10.dp))
            ) {
                if (coverUrl.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(coverUrl).crossfade(true).build(),
                        contentDescription = "Cover",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = ComicYellow,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(ComicYellow, RoundedCornerShape(6.dp))
                            .border(1.dp, ComicInk, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = story.tone.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = ComicInk
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${story.panels.size} Panels",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = story.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = ComicInk,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = story.synopsis,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = dateStr,
                    fontSize = 9.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Medium
                )
            }

            // Action Icons (Fav & Delete)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (story.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (story.isFavorite) ComicRed else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
