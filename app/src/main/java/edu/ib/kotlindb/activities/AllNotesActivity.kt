package edu.ib.kotlindb.activities

import DatabaseHandler
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.ib.kotlindb.R
import edu.ib.kotlindb.database.NoteAdapter
import edu.ib.kotlindb.models.Note
import edu.ib.kotlindb.models.TextNote
import kotlinx.android.synthetic.main.dialog_add_new_text_note.*

class AllNotesActivity : AppCompatActivity() {

    internal lateinit var databaseHandler: DatabaseHandler
    internal lateinit var list: ArrayList<Note>

    internal lateinit var rvItemsList: RecyclerView
    internal lateinit var btnAdd: FloatingActionButton


    internal lateinit var btnChoose: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_notes)
        supportActionBar?.title = "All Notes"

        rvItemsList = findViewById(R.id.rvAllTextNotes)

        btnAdd = findViewById(R.id.btnAddNewNote)
        btnChoose = findViewById(R.id.btnChoose)
        setupListofDataIntoRecyclerView()


        btnAdd.setOnClickListener { view ->

            addNewTextNoteAlertDialog()
            setupListofDataIntoRecyclerView()
        }
        btnChoose.setOnClickListener {

            Toast.makeText(this,"TA click on text",Toast.LENGTH_SHORT).show()
            chooseNoteType()
        }



    }
    private fun getBasicItemsList(): ArrayList<Note> {
        //creating the instance of DatabaseHandler class
        databaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        list = databaseHandler.viewNotes()
        return list
    }



    private fun setupListofDataIntoRecyclerView() {
        if (getBasicItemsList().size == 0) {
            rvItemsList.visibility = View.GONE
        }
        refreshAdapter()
    }


    private fun refreshAdapter() {
        rvItemsList.layoutManager = LinearLayoutManager(this)
        val itemAdapter = NoteAdapter(this, getBasicItemsList())
        rvItemsList.adapter = itemAdapter
        itemAdapter.notifyDataSetChanged()
    }


    fun deleteRecordAlertDialog(empModelClass: Note) {
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

    private fun chooseNoteType() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_choose_new_note_type)

        customDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customDialog.window?.setBackgroundDrawableResource(R.drawable.round_corner);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.show()
    }

    private fun addNewTextNoteAlertDialog() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_add_new_text_note)

        customDialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner);
        val addBtn = customDialog.findViewById(R.id.btn_add) as TextView
//        val noBtn = customDialog.findViewById(R.id.no_opt) as TextView
        databaseHandler = DatabaseHandler(this)
        customDialog!!.setCanceledOnTouchOutside(true);

        addBtn.setOnClickListener {
            //Do something here
            val title = customDialog.et_title.text.toString()
            val desc = customDialog.et_desc.text.toString()


            databaseHandler.addTextNote(TextNote(0, title, desc))
            setupListofDataIntoRecyclerView()
            Toast.makeText(this,"Added new note ${title}",Toast.LENGTH_SHORT).show()

            customDialog.dismiss()
        }
        customDialog.show()


    }
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_text_note ->
                    if (checked) {
                        Toast.makeText(view.context,"Dialog Radio: text",Toast.LENGTH_LONG).show()
                        addNewTextNoteAlertDialog()
                    }
                R.id.radio_photo ->
                    if (checked) {
                        Toast.makeText(view.context,"Dialog Radio: photo",Toast.LENGTH_LONG).show()
                        val intent = Intent(this, PhotoNoteActivity::class.java)
                        this.startActivity(intent)
                    }
            }
        }
    }

}