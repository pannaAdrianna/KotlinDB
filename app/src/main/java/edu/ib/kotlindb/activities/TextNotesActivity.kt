package edu.ib.kotlindb.activities

import DatabaseHandler
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.ib.kotlindb.R
import edu.ib.kotlindb.database.EmpModelClass
import edu.ib.kotlindb.database.TextNotesAdapter
import edu.ib.kotlindb.models.TextNote
import kotlinx.android.synthetic.main.dialog_add_new_text_note.*

class TextNotesActivity : AppCompatActivity() {


    internal lateinit var databaseHandler: DatabaseHandler
    internal lateinit var empList: ArrayList<TextNote>

    internal lateinit var rvItemsList: RecyclerView
    internal lateinit var btnAdd: FloatingActionButton

    val TAG: String = "TextNotesActivity: "


    internal var searchView: SearchView? = null
    val requestCSVcode = 1
    internal lateinit var myList: ArrayList<HashMap<String, String>>
    private var adapter: TextNotesAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_notes)
        supportActionBar?.setTitle("Notes")

        // to z content
        rvItemsList = findViewById(R.id.rvTextNotes)
        btnAdd = findViewById(R.id.btnAddNewNote)
        searchView = findViewById(R.id.search_bar)
        setupListofDataIntoRecyclerView()
        adapter = TextNotesAdapter(this, getBasicItemsList())
        rvItemsList.adapter = adapter;


        btnAdd.setOnClickListener { view ->
            /*  Toast.makeText(
                  applicationContext,
                  TAG + " btn Add click",
                  Toast.LENGTH_LONG
              ).show()*/


//            openDialog()
            showDialog()
            setupListofDataIntoRecyclerView()
//            TODO("alert dialog")
        }

    }

   /* fun openDialog() {
        val dialog = AddNewNoteDialog()
        dialog.show(supportFragmentManager, "Add New Note Dialog")

        println("\n\n\n note from dialog ${note}")
        if (note.title != "") {
            databaseHandler.addTextNote(note)
            setupListofDataIntoRecyclerView()
            dialog.dismiss()
        }


    }*/


    private fun getBasicItemsList(): ArrayList<TextNote> {
        //creating the instance of DatabaseHandler class
        databaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        empList = databaseHandler.viewTextNote()

        return empList
    }


    private fun setupListofDataIntoRecyclerView() {

        val tvNoRecordsAvailable: TextView = findViewById(R.id.tvNoRecordsAvailable)
        if (getBasicItemsList().size == 0) {
            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
            addSomeData()
        }
        refreshAdapter()
    }


    private fun refreshAdapter() {
        rvItemsList.layoutManager = LinearLayoutManager(this)
        val itemAdapter = TextNotesAdapter(this, getBasicItemsList())
        rvItemsList.adapter = itemAdapter
        itemAdapter.notifyDataSetChanged()
    }

    private fun addSomeData() {
        databaseHandler.addTextNote(
            TextNote(1, "Title Test 1", "DESC test 1")
        )
        databaseHandler.addTextNote(
            TextNote(2, "Title Test 2", "DESC test 2")
        )
        databaseHandler.addTextNote(
            TextNote(3, "Title Test 3", "DESC test 3")
        )
    }


    private fun showDialog() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_add_new_text_note)

        customDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val yesBtn = customDialog.findViewById(R.id.btn_add) as TextView
//        val noBtn = customDialog.findViewById(R.id.no_opt) as TextView
        databaseHandler = DatabaseHandler(this)
        customDialog!!.setCanceledOnTouchOutside(true);

        yesBtn.setOnClickListener {
            //Do something here
            val title = customDialog.et_title.text.toString()
            val desc = customDialog.et_desc.text.toString()


            databaseHandler.addTextNote(TextNote(0, title, desc))
            setupListofDataIntoRecyclerView()
            Toast.makeText(this,"Added new note ${title}",Toast.LENGTH_SHORT).show()

            customDialog.dismiss()
        }
//        noBtn.setOnClickListener {
//            customDialog.dismiss()
//        }
        customDialog.show()
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