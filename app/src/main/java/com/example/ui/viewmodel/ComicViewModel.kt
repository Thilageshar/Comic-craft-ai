package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ComicDatabase
import com.example.data.model.ComicPanel
import com.example.data.model.ComicStory
import com.example.data.remote.ComicStoryGenerator
import com.example.data.remote.GeminiComicService
import com.example.data.repository.ComicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

data class CreateFormState(
    val prompt: String = "",
    val characterName: String = "",
    val setting: String = "Campus Lab",
    val tone: String = "Adventure",
    val artStyle: String = "Classic Comic Book",
    val panelCount: Int = 4,
    val isGenerating: Boolean = false,
    val statusMessage: String = ""
)

class ComicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ComicRepository

    init {
        val db = ComicDatabase.getInstance(application)
        repository = ComicRepository(db.comicDao())
    }

    val savedComics: StateFlow<List<ComicStory>> = repository.allComics.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteComics: StateFlow<List<ComicStory>> = repository.favoriteComics.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _formState = MutableStateFlow(CreateFormState())
    val formState: StateFlow<CreateFormState> = _formState.asStateFlow()

    private val _currentComic = MutableStateFlow<ComicStory?>(null)
    val currentComic: StateFlow<ComicStory?> = _currentComic.asStateFlow()

    private val _activePanelForEdit = MutableStateFlow<ComicPanel?>(null)
    val activePanelForEdit: StateFlow<ComicPanel?> = _activePanelForEdit.asStateFlow()

    fun updatePrompt(value: String) {
        _formState.value = _formState.value.copy(prompt = value)
    }

    fun updateCharacterName(value: String) {
        _formState.value = _formState.value.copy(characterName = value)
    }

    fun updateSetting(value: String) {
        _formState.value = _formState.value.copy(setting = value)
    }

    fun updateTone(value: String) {
        _formState.value = _formState.value.copy(tone = value)
    }

    fun updateArtStyle(value: String) {
        _formState.value = _formState.value.copy(artStyle = value)
    }

    fun updatePanelCount(value: Int) {
        _formState.value = _formState.value.copy(panelCount = value)
    }

    fun useExamplePrompt(prompt: String, charName: String, setting: String, tone: String) {
        _formState.value = _formState.value.copy(
            prompt = prompt,
            characterName = charName,
            setting = setting,
            tone = tone
        )
    }

    fun generateComic(onSuccess: () -> Unit = {}) {
        val form = _formState.value
        if (form.prompt.isBlank()) return

        viewModelScope.launch {
            _formState.value = _formState.value.copy(
                isGenerating = true,
                statusMessage = "Calling Gemini AI for plot & dialogues..."
            )

            val generated = GeminiComicService.generateComic(
                prompt = form.prompt.trim(),
                characterName = form.characterName.trim(),
                setting = form.setting.trim(),
                tone = form.tone,
                artStyle = form.artStyle,
                panelCount = form.panelCount
            )

            // Save directly into Room
            val id = repository.saveComic(generated)
            val savedWithId = generated.copy(id = id)

            _currentComic.value = savedWithId
            _formState.value = _formState.value.copy(
                isGenerating = false,
                statusMessage = "Comic created successfully!"
            )
            onSuccess()
        }
    }

    fun rerollPanelImage(panelIndex: Int) {
        val comic = _currentComic.value ?: return
        if (panelIndex !in comic.panels.indices) return

        val targetPanel = comic.panels[panelIndex]
        val newSeed = Random.nextInt(999999)
        val newImgUrl = ComicStoryGenerator.buildImageUrl(targetPanel.visualPrompt, comic.artStyle, newSeed)

        val updatedPanels = comic.panels.toMutableList()
        updatedPanels[panelIndex] = targetPanel.copy(imageUrl = newImgUrl)

        val updatedStory = comic.copy(panels = updatedPanels)
        _currentComic.value = updatedStory

        viewModelScope.launch {
            repository.updateComic(updatedStory)
        }
    }

    fun selectPanelForEdit(panel: ComicPanel?) {
        _activePanelForEdit.value = panel
    }

    fun saveUpdatedPanel(updatedPanel: ComicPanel) {
        val comic = _currentComic.value ?: return
        val updatedPanels = comic.panels.map {
            if (it.panelNumber == updatedPanel.panelNumber) updatedPanel else it
        }
        val updatedStory = comic.copy(panels = updatedPanels)
        _currentComic.value = updatedStory
        _activePanelForEdit.value = null

        viewModelScope.launch {
            repository.updateComic(updatedStory)
        }
    }

    fun loadComic(story: ComicStory) {
        _currentComic.value = story
    }

    fun toggleFavorite(story: ComicStory) {
        val newFav = !story.isFavorite
        if (_currentComic.value?.id == story.id) {
            _currentComic.value = _currentComic.value?.copy(isFavorite = newFav)
        }
        viewModelScope.launch {
            repository.setFavorite(story.id, newFav)
        }
    }

    fun deleteComic(story: ComicStory) {
        if (_currentComic.value?.id == story.id) {
            _currentComic.value = null
        }
        viewModelScope.launch {
            repository.deleteComic(story)
        }
    }

    fun shareComicScript(context: Context, story: ComicStory) {
        val sb = StringBuilder()
        sb.append("=== ${story.title} ===\n")
        sb.append("${story.synopsis}\n\n")
        sb.append("Tone: ${story.tone} | Art Style: ${story.artStyle}\n\n")

        story.panels.forEach { p ->
            sb.append("[PANEL ${p.panelNumber}]\n")
            if (p.caption.isNotBlank()) sb.append("NARRATION: ${p.caption}\n")
            if (p.dialogue.isNotBlank()) sb.append("${p.speaker.ifBlank { "HERO" }}: \"${p.dialogue}\"\n")
            if (p.soundEffect.isNotBlank()) sb.append("SFX: *${p.soundEffect}*\n")
            sb.append("SCENE: ${p.visualPrompt}\n\n")
        }
        sb.append("Created with ComicCraft AI")

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, sb.toString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Comic Script")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}
