package edu.ib.kotlindb.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.ib.kotlindb.R
import edu.ib.kotlindb.userInfo.AccountDashboard
import android.database.CursorWindow
import java.lang.Exception
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // do testów aktywności
    fun onBtnTestClick(view: android.view.View) {

        val intent = Intent(this, TextNotesActivity::class.java)
        this.startActivity(intent)
    }

    fun onBtnPhotoNotesClick(view: View) {
//        val intent = Intent(this, TestList::class.java)
        val intent = Intent(this, AllNotesActivity::class.java)
        this.startActivity(intent)


    }



}