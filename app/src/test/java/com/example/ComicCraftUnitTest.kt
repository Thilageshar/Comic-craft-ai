package com.example

import com.example.data.local.ComicConverters
import com.example.data.model.ComicPanel
import com.example.data.remote.ComicStoryGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ComicCraftUnitTest {

    @Test
    fun testComicStoryGenerator_createsValidComic() {
        val story = ComicStoryGenerator.generateFallbackComic(
            prompt = "A hero discovers a cosmic artifact in the lab",
            characterName = "Leo",
            setting = "Cyber Lab",
            tone = "Action",
            artStyle = "Classic Comic Book",
            panelCount = 4
        )

        assertNotNull(story)
        assertEquals(4, story.panels.size)
        assertTrue(story.title.contains("LEO") || story.title.isNotBlank())
        assertEquals("Action", story.tone)
        assertEquals("Classic Comic Book", story.artStyle)
        story.panels.forEach { panel ->
            assertTrue(panel.caption.isNotBlank())
            assertTrue(panel.dialogue.isNotBlank())
            assertTrue(panel.imageUrl.isNotBlank())
        }
    }

    @Test
    fun testComicConverters_serializesAndDeserializes() {
        val converters = ComicConverters()
        val originalPanels = listOf(
            ComicPanel(
                panelNumber = 1,
                caption = "In the dark of night...",
                speaker = "HERO",
                dialogue = "Who goes there?",
                visualPrompt = "Hero looking around in shadows",
                soundEffect = "SNAP!",
                imageUrl = "https://example.com/panel1.jpg"
            ),
            ComicPanel(
                panelNumber = 2,
                caption = "A sudden flash!",
                speaker = "VILLAIN",
                dialogue = "It is I!",
                visualPrompt = "Villain emerges with glowing red eyes",
                soundEffect = "KABOOM!",
                imageUrl = "https://example.com/panel2.jpg"
            )
        )

        val jsonString = converters.fromPanelsList(originalPanels)
        assertTrue(jsonString.contains("In the dark of night..."))
        assertTrue(jsonString.contains("KABOOM!"))

        val parsed = converters.toPanelsList(jsonString)
        assertEquals(2, parsed.size)
        assertEquals(1, parsed[0].panelNumber)
        assertEquals("In the dark of night...", parsed[0].caption)
        assertEquals("SNAP!", parsed[0].soundEffect)
        assertEquals(2, parsed[1].panelNumber)
        assertEquals("KABOOM!", parsed[1].soundEffect)
    }
}
