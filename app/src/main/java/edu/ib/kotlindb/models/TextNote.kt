package edu.ib.kotlindb.models

import java.time.LocalDateTime

data class TextNote(override val id: Int, override var title: String?, val desc: String? = null, override var createdAt: LocalDateTime) : Note(id, title, createdAt) {

}