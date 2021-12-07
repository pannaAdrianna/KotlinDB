package edu.ib.kotlindb.models

data class TextNote(override val id: Int, override var title: String?, val desc: String? = null) : Note(id, title) {

}