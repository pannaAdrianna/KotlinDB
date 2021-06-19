package edu.ib.kotlindb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AnalyzeINCI : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyze_i_n_c_i)
    }

    fun onBtnAnalyzeClick(view: View) {}
}