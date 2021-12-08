package edu.ib.kotlindb.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class Note(open val id: Int = -1, open var title: String? = null, open var createdAt:LocalDateTime= LocalDateTime.now()) {


    var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    override fun toString(): String {
        return "Note \nid=$id, title=$title"
    }

    private fun editDate(newDate: LocalDateTime) {
        createdAt = LocalDateTime.parse(formatter.format(newDate))
    }

    fun getDate(): LocalDateTime {
        return createdAt;
    }
    fun getFormattedDate(): String {
        return formatter.format(createdAt);
    }


}

