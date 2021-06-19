package edu.ib.kotlindb

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun onBtnSearchIngredient(view: View) {
        val intent = Intent(this, ListActivity::class.java)
        this.startActivity(intent)


    }
    fun onBtnAnalyzeClick(view: View) {
        val intent = Intent(this, AnalyzeINCI::class.java)
        this.startActivity(intent)
    }

    fun onBtnOCRClick(view: View) {}
    fun onBtnAccountClick(view: View) {
        val intent = Intent(this, AccountDashboard::class.java)
        this.startActivity(intent)

    }


}