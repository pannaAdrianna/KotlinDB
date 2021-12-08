package edu.ib.kotlindb.models

import java.time.LocalDateTime

data class PhotoNote(override val id: Int, override var title: String?, val desc: String? = null, val image: ByteArray,override var createdAt: LocalDateTime,override var modificationDate: LocalDateTime): Note(id, title, createdAt, modificationDate) {

}