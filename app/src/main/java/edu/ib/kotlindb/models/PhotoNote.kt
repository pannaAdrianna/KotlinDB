package edu.ib.kotlindb.models

data class PhotoNote(override val id: Int, override var title: String?, val desc: String? = null, val image: ByteArray): Note(id, title) {
}