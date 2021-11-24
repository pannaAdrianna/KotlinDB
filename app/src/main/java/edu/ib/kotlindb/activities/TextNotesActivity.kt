package edu.ib.kotlindb.activities

import DatabaseHandler
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ib.kotlindb.R
import edu.ib.kotlindb.database.EmpModelClass
import edu.ib.kotlindb.database.TextNotesAdapter
import edu.ib.kotlindb.models.TextNote

class TextNotesActivity : AppCompatActivity() {


    internal lateinit var databaseHandler: DatabaseHandler
    internal lateinit var empList: ArrayList<TextNote>

    internal lateinit var rvItemsList: RecyclerView
    internal var searchView: SearchView? = null
    val requestCSVcode = 1
    internal lateinit var myList: ArrayList<HashMap<String, String>>
    private var adapter: TextNotesAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_notes)
        getSupportActionBar()?.setTitle("Notes")

        // to z content
        rvItemsList = findViewById(R.id.rvTextNotes)
        searchView = findViewById(R.id.search_bar)
        setupListofDataIntoRecyclerView()
        adapter = TextNotesAdapter(this, getBasicItemsList())
        rvItemsList.setAdapter(adapter);

    }

    private fun getBasicItemsList(): ArrayList<TextNote> {
        //creating the instance of DatabaseHandler class
        databaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        empList = databaseHandler.viewTextNote()

        return empList
    }


    private fun setupListofDataIntoRecyclerView() {
        rvItemsList = findViewById(R.id.rvTextNotes)
        val tvNoRecordsAvailable: TextView = findViewById(R.id.tvNoRecordsAvailable)

//        if (getBasicItemsList().size == 0) {
//            readFromCSV()
//        }
//        if (getPreggoItemsList().size == 0) {
//            readFromCSV2()
//        }

        if (getBasicItemsList().size == 0) {
            rvItemsList.visibility = View.INVISIBLE
        } else {
            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE
        }
        rvItemsList.layoutManager = LinearLayoutManager(this)
        val itemAdapter = TextNotesAdapter(this, getBasicItemsList())
        rvItemsList.adapter = itemAdapter


    }

    fun deleteRecordAlertDialog(empModelClass: TextNote) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${empModelClass.title}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            databaseHandler = DatabaseHandler(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteTextNote(
                TextNote(
                    empModelClass.id,
                    "",
                    ""
                )
            )
            if (status > -1) {
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()

                setupListofDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }


}