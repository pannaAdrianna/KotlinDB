package edu.ib.kotlindb

import DatabaseHandler
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_main.*

class AnalyzeActivity : AppCompatActivity() {

    lateinit var etIngredients: EditText
    lateinit var tvResult: TextView
    internal lateinit var databaseHandler: DatabaseHandler



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyze_)

        var etIngredients: EditText = findViewById(R.id.etINCIstr)
        var tvResult: TextView = findViewById(R.id.tvControversialngriedients)
        databaseHandler = DatabaseHandler(this)

        val btnAnalyze = findViewById<Button>(R.id.btnAnalyze)
        btnAnalyze.setOnClickListener { view ->
            val test = etIngredients.getText().toString()
            tvResult.setText(test);
            etIngredients.text.clear()

        }
    }


}


