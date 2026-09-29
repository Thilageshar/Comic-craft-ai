package com.example.data.remote

import com.example.data.model.ComicPanel
import com.example.data.model.ComicStory
import kotlin.random.Random

object ComicStoryGenerator {

    private val soundEffectsList = listOf(
        "POW!", "BAM!", "ZAP!", "KABOOM!", "WHOOSH!", "CLICK!", "BZZZT!", "GASP!", "CRASH!", "WHAM!", "THUMP!"
    )

    fun buildImageUrl(visualPrompt: String, artStyle: String, seed: Int = Random.nextInt(100000)): String {
        val styleKeywords = when (artStyle) {
            "Classic Comic Book" -> "vintage American comic book art, halftone dot printing, thick black outlines, retro pop-art colors"
            "Anime & Manga" -> "Japanese manga anime style, sharp action lines, cel shaded, highly detailed graphic novel"
            "Saturday Cartoon" -> "playful Saturday morning cartoon style, vibrant expressive animation, fun colorful"
            "Pixar 3D Animation" -> "cinematic 3D animation style, Pixar inspired, soft volumetric lighting, vivid textures"
            "Vibrant Watercolor" -> "expressive comic watercolor illustration, rich brush splatters, inked outlines"
            "Gritty Graphic Novel" -> "gritty graphic novel noir, stark ink shadows, dramatic high contrast lighting"
            else -> "comic book panel illustration, bold colors, dynamic angle, high resolution"
        }
        val encodedPrompt = java.net.URLEncoder.encode("$visualPrompt, $styleKeywords", "UTF-8")
        return "https://image.pollinations.ai/prompt/$encodedPrompt?width=600&height=450&seed=$seed&nologo=true"
    }

    fun generateFallbackComic(
        prompt: String,
        characterName: String,
        setting: String,
        tone: String,
        artStyle: String,
        panelCount: Int
    ): ComicStory {
        val hero = if (characterName.isNotBlank()) characterName else "Hero"
        val place = if (setting.isNotBlank()) setting else "Neo Metropolis"

        val titles = when (tone) {
            "Funny" -> listOf("THE RIDICULOUS TALE OF $hero", "CHAOS IN $place!", "$hero'S EPIC BLUNDER")
            "Mystery" -> listOf("THE ENIGMA OF $place", "SHADOWS OVER $hero", "CASE OF THE MISSING SIGNAL")
            "Action" -> listOf("$hero: STRIKE AT $place!", "MAXIMUM OVERDRIVE", "CLASH OF TITANS")
            "Spooky" -> listOf("NIGHTMARE IN $place", "THE CURSE OF $hero", "WHISPERS IN THE DARK")
            else -> listOf("THE CHRONICLES OF $hero", "ADVENTURES IN $place", "THE AWAKENING AT $place")
        }
        val chosenTitle = titles[Random.nextInt(titles.size)].uppercase()

        val synopsis = "A high-stakes $tone story where $hero takes on the unexpected in $place."

        val panels = mutableListOf<ComicPanel>()

        for (i in 1..panelCount) {
            val (caption, speaker, dialogue, visualDesc, sfx) = when (i) {
                1 -> StoryBeat(
                    caption = "Day begins quietly in the heart of $place...",
                    speaker = hero,
                    dialogue = "Another routine day... or so I thought.",
                    visualPrompt = "Wide cinematic shot of $hero walking through $place, noticing a strange shimmering light",
                    sfx = "HUMMM..."
                )
                2 -> StoryBeat(
                    caption = "Suddenly, an unexpected disturbance ripples through the air!",
                    speaker = hero,
                    dialogue = "Hold on! What is that energy reading?!",
                    visualPrompt = "Close-up action angle of $hero turning around in shock as an explosion of sparks erupts",
                    sfx = "ZAP!"
                )
                3 -> StoryBeat(
                    caption = "Without warning, the confrontation unfolds in seconds.",
                    speaker = "MYSTERIOUS RIVAL",
                    dialogue = "You're too late, $hero! The power is already ours!",
                    visualPrompt = "Dynamic confrontation scene between $hero and a shadowy figure in $place, high tension",
                    sfx = "POW!"
                )
                4 -> {
                    if (panelCount == 4) {
                        StoryBeat(
                            caption = "With a decisive strike, $hero restores harmony to $place!",
                            speaker = hero,
                            dialogue = "Not on my watch! Victory is ours!",
                            visualPrompt = "Heroic triumph stance of $hero standing atop the ruins of $place with sparkling energy in the sky",
                            sfx = "VICTORY!"
                        )
                    } else {
                        StoryBeat(
                            caption = "The battle escalates as the stakes grow higher.",
                            speaker = hero,
                            dialogue = "I need to tap into the core generator now!",
                            visualPrompt = "Hero sprinting toward a glowing console while dodging energy beams in $place",
                            sfx = "KABOOM!"
                        )
                    }
                }
                5 -> StoryBeat(
                    caption = "Pushing beyond normal limits into uncharted territory.",
                    speaker = hero,
                    dialogue = "I won't let this city fall!",
                    visualPrompt = "Dramatic upward perspective of $hero unleashing a burst of vibrant aura",
                    sfx = "WHOOSH!"
                )
                6 -> {
                    if (panelCount == 6) {
                        StoryBeat(
                            caption = "The dust settles, and a legend is born.",
                            speaker = hero,
                            dialogue = "This was just chapter one.",
                            visualPrompt = "Epic silhouette of $hero overlooking the dawn over $place",
                            sfx = "WHAM!"
                        )
                    } else {
                        StoryBeat(
                            caption = "A sudden twist threatens everything they fought for.",
                            speaker = "ANNOUNCER",
                            dialogue = "WARNING: System overload imminent!",
                            visualPrompt = "Flashing sirens and red emergency alarms in $place",
                            sfx = "BZZZT!"
                        )
                    }
                }
                7 -> StoryBeat(
                    caption = "In the final seconds, one desperate gamble remains.",
                    speaker = hero,
                    dialogue = "All or nothing... here goes everything!",
                    visualPrompt = "$hero leaping forward with full resolve amidst cracking electrical lightning",
                    sfx = "CRASH!"
                )
                else -> StoryBeat(
                    caption = "Peace returns, but the horizon whispers of future adventures.",
                    speaker = hero,
                    dialogue = "Until next time, $place.",
                    visualPrompt = "Majestic comic book splash panel of $hero in heroic pose with the sun setting behind $place",
                    sfx = "FIN!"
                )
            }

            val seed = Random.nextInt(999999)
            val imgUrl = buildImageUrl(visualDesc, artStyle, seed)

            panels.add(
                ComicPanel(
                    panelNumber = i,
                    caption = caption,
                    speaker = speaker,
                    dialogue = dialogue,
                    visualPrompt = visualDesc,
                    soundEffect = sfx,
                    imageUrl = imgUrl
                )
            )
        }

        return ComicStory(
            title = chosenTitle,
            synopsis = synopsis,
            prompt = prompt,
            characterName = hero,
            setting = place,
            tone = tone,
            artStyle = artStyle,
            panelCount = panelCount,
            panels = panels
        )
    }

    private data class StoryBeat(
        val caption: String,
        val speaker: String,
        val dialogue: String,
        val visualPrompt: String,
        val sfx: String
    )
}
