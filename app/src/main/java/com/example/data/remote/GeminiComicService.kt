package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.ComicPanel
import com.example.data.model.ComicStory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object GeminiComicService {
    private const val TAG = "GeminiComicService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generateComic(
        prompt: String,
        characterName: String,
        setting: String,
        tone: String,
        artStyle: String,
        panelCount: Int
    ): ComicStory = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY" || apiKey.length < 10) {
            Log.d(TAG, "No valid Gemini API key found, generating with built-in creative engine")
            return@withContext ComicStoryGenerator.generateFallbackComic(
                prompt = prompt,
                characterName = characterName,
                setting = setting,
                tone = tone,
                artStyle = artStyle,
                panelCount = panelCount
            )
        }

        try {
            val systemInstruction = """
                You are ComicCraft, an elite comic book writer and storyboard artist.
                Create a dynamic, expressive comic strip script based strictly on the user specifications.
                Return ONLY valid JSON matching this exact schema:
                {
                  "title": "Short punchy comic title in ALL CAPS",
                  "synopsis": "One sentence summary of the storyline",
                  "panels": [
                    {
                      "panelNumber": 1,
                      "caption": "Atmospheric narrator caption or place setting",
                      "speaker": "Name of character speaking or NARRATOR",
                      "dialogue": "Character dialogue line",
                      "visualPrompt": "Detailed visual description of this panel (characters, action, camera angle, lighting)",
                      "soundEffect": "Onomatopoeia action text like POW!, ZAP!, WHOOSH!, or empty"
                    }
                  ]
                }
            """.trimIndent()

            val userPrompt = """
                Story Idea: $prompt
                Main Character: ${if (characterName.isNotBlank()) characterName else "Protagonist"}
                Setting: ${if (setting.isNotBlank()) setting else "City"}
                Tone: $tone
                Art Style: $artStyle
                Total Panels: $panelCount
            """.trimIndent()

            val requestJson = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", "$systemInstruction\n\n$userPrompt")
                            })
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)

                val genConfig = JSONObject().apply {
                    put("temperature", 0.7)
                    put("responseMimeType", "application/json")
                }
                put("generationConfig", genConfig)
            }

            val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.w(TAG, "Gemini API error ${response.code}: ${response.message}")
                return@withContext ComicStoryGenerator.generateFallbackComic(
                    prompt = prompt,
                    characterName = characterName,
                    setting = setting,
                    tone = tone,
                    artStyle = artStyle,
                    panelCount = panelCount
                )
            }

            val responseBody = response.body?.string() ?: ""
            val jsonRoot = JSONObject(responseBody)
            val candidates = jsonRoot.optJSONArray("candidates")
            val candidate = candidates?.optJSONObject(0)
            val content = candidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val textPart = parts?.optJSONObject(0)?.optString("text") ?: ""

            // Parse generated comic JSON
            val comicJson = JSONObject(textPart)
            val title = comicJson.optString("title", "UNTITLED COMIC").uppercase()
            val synopsis = comicJson.optString("synopsis", "An thrilling comic adventure.")
            val panelsArray = comicJson.optJSONArray("panels")

            val parsedPanels = mutableListOf<ComicPanel>()
            if (panelsArray != null && panelsArray.length() > 0) {
                for (i in 0 until panelsArray.length()) {
                    val pObj = panelsArray.getJSONObject(i)
                    val pNum = pObj.optInt("panelNumber", i + 1)
                    val caption = pObj.optString("caption", "")
                    val speaker = pObj.optString("speaker", "")
                    val dialogue = pObj.optString("dialogue", "")
                    val visual = pObj.optString("visualPrompt", "$tone scene in $setting")
                    val sfx = pObj.optString("soundEffect", "")
                    val imgUrl = ComicStoryGenerator.buildImageUrl(visual, artStyle, Random.nextInt(999999))

                    parsedPanels.add(
                        ComicPanel(
                            panelNumber = pNum,
                            caption = caption,
                            speaker = speaker,
                            dialogue = dialogue,
                            visualPrompt = visual,
                            soundEffect = sfx,
                            imageUrl = imgUrl
                        )
                    )
                }
            }

            if (parsedPanels.isEmpty()) {
                return@withContext ComicStoryGenerator.generateFallbackComic(
                    prompt = prompt,
                    characterName = characterName,
                    setting = setting,
                    tone = tone,
                    artStyle = artStyle,
                    panelCount = panelCount
                )
            }

            return@withContext ComicStory(
                title = title,
                synopsis = synopsis,
                prompt = prompt,
                characterName = characterName,
                setting = setting,
                tone = tone,
                artStyle = artStyle,
                panelCount = parsedPanels.size,
                panels = parsedPanels
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error generating comic via Gemini API: ${e.message}", e)
            return@withContext ComicStoryGenerator.generateFallbackComic(
                prompt = prompt,
                characterName = characterName,
                setting = setting,
                tone = tone,
                artStyle = artStyle,
                panelCount = panelCount
            )
        }
    }
}
