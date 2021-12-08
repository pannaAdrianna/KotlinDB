package edu.ib.kotlindb.activities

import DatabaseHandler
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.ib.kotlindb.R
import edu.ib.kotlindb.database.TextNotesAdapter
import edu.ib.kotlindb.models.TextNote
import kotlinx.android.synthetic.main.dialog_add_new_text_note.*
import java.time.LocalDateTime

class TextNotesActivity : AppCompatActivity() {


    internal lateinit var databaseHandler: DatabaseHandler
    internal lateinit var list: ArrayList<TextNote>

    internal lateinit var rvItemsList: RecyclerView
    internal lateinit var btnAdd: FloatingActionButton
    internal lateinit var btnChoose: FloatingActionButton



    internal var searchView: SearchView? = null
    val requestCSVcode = 1
    internal lateinit var myList: ArrayList<HashMap<String, String>>
    private var adapter: TextNotesAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_notes)
        supportActionBar?.title = "Text Notes"

        // to z content
        rvItemsList = findViewById(R.id.rvTextNotes)
        btnAdd = findViewById(R.id.btnAddNewNote)
        btnChoose = findViewById(R.id.btnChoose)
        searchView = findViewById(R.id.search_bar)
        setupListofDataIntoRecyclerView()
        adapter = TextNotesAdapter(this, getBasicItemsList())
        rvItemsList.adapter = adapter;


        btnAdd.setOnClickListener { view ->

            addNewTextNoteAlertDialog()
            setupListofDataIntoRecyclerView()
        }
        btnChoose.setOnClickListener {

            Toast.makeText(this,"TA click on text",Toast.LENGTH_SHORT).show()
            chooseNoteType()
        }

    }



    private fun getBasicItemsList(): ArrayList<TextNote> {
        //creating the instance of DatabaseHandler class
        databaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        list = databaseHandler.viewTextNote()
        return list
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
            TextNote(1, "Title Test 1", "DESC test 1", LocalDateTime.now())
        )
        databaseHandler.addTextNote(
            TextNote(2, "Title Test 2", "DESC test 2",LocalDateTime.now())
        )
        databaseHandler.addTextNote(
            TextNote(3, "Title Test 3", "DESC test 3",LocalDateTime.now())
        )
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


            databaseHandler.addTextNote(TextNote(0, title, desc, LocalDateTime.now()))
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
                        // Ninjas rule
                        Toast.makeText(view.context,"Dialog Radio: photo",Toast.LENGTH_LONG).show()
                        val intent = Intent(this, PhotoNoteActivity::class.java)
                        this.startActivity(intent)
                    }
            }
        }
    }

    private fun chooseNoteType() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_choose_new_note_type)

        customDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customDialog.window?.setBackgroundDrawableResource(R.drawable.round_corner);
        customDialog.setCanceledOnTouchOutside(true);
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
                    "",
                    empModelClass.getDate()
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

    fun showDetailsOfTextNote(note: TextNote) {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_show_details)
        customDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customDialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner);
        customDialog!!.setCanceledOnTouchOutside(true);

        val title = customDialog.findViewById(R.id.tv_note_title) as TextView
        val desc = customDialog.findViewById(R.id.tv_note_desc) as TextView
        val date = customDialog.findViewById(R.id.tv_note_date) as TextView
        val btnedit = customDialog.findViewById(R.id.btn_edit_note) as Button
        val ll_image = customDialog.findViewById(R.id.ll_details_image) as LinearLayout
        title.text = note.title
        desc.text = note.desc
        date.text = note.getFormattedDate()
        ll_image.visibility=View.GONE

        databaseHandler = DatabaseHandler(this)

        btnedit.setOnClickListener {
            Toast.makeText(this, "show details edit click", Toast.LENGTH_SHORT).show()
//            updateTextNote(note)
        }

        customDialog.show()
    }


}