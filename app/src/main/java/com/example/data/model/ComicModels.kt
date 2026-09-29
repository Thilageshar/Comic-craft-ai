package com.example.data.model

data class ComicPanel(
    val panelNumber: Int,
    val caption: String = "",
    val speaker: String = "",
    val dialogue: String = "",
    val visualPrompt: String = "",
    val soundEffect: String = "",
    val imageUrl: String = ""
)

data class ComicStory(
    val id: Long = 0,
    val title: String,
    val synopsis: String,
    val prompt: String,
    val characterName: String,
    val setting: String,
    val tone: String,
    val artStyle: String,
    val panelCount: Int,
    val panels: List<ComicPanel>,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
