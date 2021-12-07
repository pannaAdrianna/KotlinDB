package edu.ib.kotlindb.fragment.placeholder

import edu.ib.kotlindb.models.TextNote
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    val ITEMS: MutableList<TextNote> = ArrayList()

    /**
     * A map of sample (placeholder) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, TextNote> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createTextNote(i))
        }
    }

    private fun addItem(item: TextNote) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createTextNote(position: Int): TextNote {
        return TextNote(position.toString(), "Item " + position, makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A placeholder item representing a piece of content.
     */
    data class TextNote(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}