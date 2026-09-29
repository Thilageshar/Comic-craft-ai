package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.model.ComicPanel
import com.example.data.model.ComicStory
import org.json.JSONArray
import org.json.JSONObject

@Entity(tableName = "comics")
@TypeConverters(ComicConverters::class)
data class ComicEntity(
    @PrimaryKey(autoGenerate = true)
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
) {
    fun toDomain(): ComicStory = ComicStory(
        id = id,
        title = title,
        synopsis = synopsis,
        prompt = prompt,
        characterName = characterName,
        setting = setting,
        tone = tone,
        artStyle = artStyle,
        panelCount = panelCount,
        panels = panels,
        createdAt = createdAt,
        isFavorite = isFavorite
    )

    companion object {
        fun fromDomain(story: ComicStory): ComicEntity = ComicEntity(
            id = story.id,
            title = story.title,
            synopsis = story.synopsis,
            prompt = story.prompt,
            characterName = story.characterName,
            setting = story.setting,
            tone = story.tone,
            artStyle = story.artStyle,
            panelCount = story.panelCount,
            panels = story.panels,
            createdAt = story.createdAt,
            isFavorite = story.isFavorite
        )
    }
}

class ComicConverters {
    @TypeConverter
    fun fromPanelsList(panels: List<ComicPanel>?): String {
        if (panels == null) return "[]"
        val array = JSONArray()
        for (p in panels) {
            val obj = JSONObject()
            obj.put("panelNumber", p.panelNumber)
            obj.put("caption", p.caption)
            obj.put("speaker", p.speaker)
            obj.put("dialogue", p.dialogue)
            obj.put("visualPrompt", p.visualPrompt)
            obj.put("soundEffect", p.soundEffect)
            obj.put("imageUrl", p.imageUrl)
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toPanelsList(jsonStr: String?): List<ComicPanel> {
        if (jsonStr.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<ComicPanel>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    ComicPanel(
                        panelNumber = obj.optInt("panelNumber", i + 1),
                        caption = obj.optString("caption", ""),
                        speaker = obj.optString("speaker", ""),
                        dialogue = obj.optString("dialogue", ""),
                        visualPrompt = obj.optString("visualPrompt", ""),
                        soundEffect = obj.optString("soundEffect", ""),
                        imageUrl = obj.optString("imageUrl", "")
                    )
                )
            }
        } catch (_: Exception) {
            // fallback empty
        }
        return list
    }
}
