package edu.ib.kotlindb.activities

import DatabaseHandler
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.ib.kotlindb.R
import edu.ib.kotlindb.database.EmpModelClass


class AnalyzeActivity : AppCompatActivity() {

    lateinit var etIngredients: EditText
    lateinit var tvResult: TextView
    internal lateinit var databaseHandler: DatabaseHandler
    internal lateinit var empList: ArrayList<EmpModelClass>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyze_)

        var etIngredients: EditText = findViewById(R.id.etINCIstr)
        var tvResult: TextView = findViewById(R.id.tvControversialngriedients)
        val btnAnalyze = findViewById<Button>(R.id.btnAnalyze)
        val results: MutableList<String> = ArrayList()

        databaseHandler = DatabaseHandler(this)
        empList = databaseHandler.viewEmployee()

        etIngredients.text.clear()
        results.clear()
        val intent = intent
        val message = intent.getStringExtra(OCRActivity.MESSAGE)
        etIngredients.setText(message)


        btnAnalyze.setOnClickListener { view ->

            if (TextUtils.isEmpty(etIngredients.getText())) {
                Toast.makeText(this, "Add ingredients to verify", Toast.LENGTH_SHORT).show();
                tvResult.setText("");
            } else {
                val test = etIngredients.text.toString()
                tvResult.text = test;

                val array = test.trim().split(",").toTypedArray()
                Log.i("AnalyzeActivity", array[0].toString())


                for (ingToCheck in array) {

                    for (ingredient in empList) {
                        var name = ingredient.label.trim()
                        if (ingToCheck.replace(" ","").equals(name, ignoreCase = true) and (name !in results)) {
                                results.add(name)

                        }
                    }
                }

//                tvResult.text = results.toString();
                val resultDisplay = StringBuilder()
                for (str in results) resultDisplay.append("$str, ")
                tvResult.text = resultDisplay
                results.clear()

            }


        }
    }


}

