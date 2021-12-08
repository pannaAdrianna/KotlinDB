package edu.ib.kotlindb.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class Note(open val id: Int = -1, open var title: String? = null, open var createdAt:LocalDateTime, open var modificationDate:LocalDateTime) {


    var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    override fun toString(): String {
        return "Note \nid=$id, title=$title"
    }

    private fun editDate(newDate: LocalDateTime) {
        createdAt = LocalDateTime.parse(formatter.format(modificationDate))
    }

    fun getCreationDate(): LocalDateTime {
        return createdAt;
    }
    fun getFormattedCreationDate(): String {
        return formatter.format(createdAt);
    }


    fun getFormattedModificationDate(): String {
        return formatter.format(modificationDate);
    }


}

