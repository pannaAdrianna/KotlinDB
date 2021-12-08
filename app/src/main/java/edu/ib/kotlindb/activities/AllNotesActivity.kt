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
import edu.ib.kotlindb.models.Note
import edu.ib.kotlindb.models.PhotoNote
import edu.ib.kotlindb.models.TextNote
import kotlinx.android.synthetic.main.dialog_add_new_text_note.*
import java.time.LocalDateTime
import android.graphics.BitmapFactory
import edu.ib.kotlindb.database.PhotoNoteAdapter


class AllNotesActivity : AppCompatActivity() {

    internal lateinit var databaseHandler: DatabaseHandler
    internal lateinit var list: ArrayList<PhotoNote>

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

            Toast.makeText(this, "TA click on text", Toast.LENGTH_SHORT).show()
            chooseNoteType()
        }


    }

    private fun getNotes(): ArrayList<PhotoNote> {
        //creating the instance of DatabaseHandler class
        databaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        list = databaseHandler.viewPhotoNotes()
        list.sortByDescending { it.modificationDate }
        return list
    }


    private fun setupListofDataIntoRecyclerView() {
        if (getNotes().size == 0) {
            rvItemsList.visibility = View.GONE
        }
        refreshAdapter()
    }


    private fun refreshAdapter() {
        rvItemsList.layoutManager = LinearLayoutManager(this)
        val itemAdapter = PhotoNoteAdapter(this,getNotes() )
        rvItemsList.adapter = itemAdapter
        itemAdapter.notifyDataSetChanged()
    }


    fun deleteRecordAlertDialog(note: Note) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${note.title}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            databaseHandler = DatabaseHandler(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteTextNote(
                TextNote(
                    note.id,
                    "",
                    "",
                    note.getCreationDate(),
                    note.modificationDate
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

    fun showDetailsOfPhotoNote(note: PhotoNote) {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_show_details)
        customDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        customDialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner);
        customDialog!!.setCanceledOnTouchOutside(true);

        val title = customDialog.findViewById(R.id.tv_note_title) as TextView
        val desc = customDialog.findViewById(R.id.tv_note_desc) as TextView
        val date = customDialog.findViewById(R.id.tv_note_date) as TextView
        val modificationDate = customDialog.findViewById(R.id.tv_note_date_modified) as TextView
        val btnedit = customDialog.findViewById(R.id.btn_edit_note) as Button
        val ll_image = customDialog.findViewById(R.id.ll_details_image) as LinearLayout
        val image = customDialog.findViewById(R.id.iv_note_image) as ImageView

        ll_image.visibility=View.VISIBLE
        image.visibility=View.VISIBLE

        title.text = note.title
        desc.text = note.desc
        date.text = note.getFormattedCreationDate()
        modificationDate.text = note.getFormattedModificationDate()
        val bitmap = BitmapFactory.decodeByteArray(note.image, 0, note.image.size)
        image.setImageBitmap(bitmap)


        databaseHandler = DatabaseHandler(this)

        btnedit.setOnClickListener {
            Toast.makeText(this, "Edit click", Toast.LENGTH_SHORT).show()
            note.modificationDate= LocalDateTime.now()
//            databaseHandler.updateTextNote(note)
            modificationDate.text = note.getFormattedModificationDate()

        }

        customDialog.show()
    }


   /* fun updateTextNote(note: PhotoNote) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)

        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.et_title.setText(note.title)
        updateDialog.et_desc.setText(note.desc)

        updateDialog.btnUpdate.setOnClickListener(View.OnClickListener {

            val new_title = updateDialog.et_title.text.toString()
            val new_desc = updateDialog.et_desc.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (!new_title.isEmpty() && !new_desc.isEmpty()) {
                val status =
                    databaseHandler.updatePhotoNote(
                        PhotoNote(
                            note.id,
                            new_title,
                            new_desc,
                            note.image,
                            note.createdAt,
                            note.modificationDate
                        )
                    )
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()

                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or Email cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        updateDialog.btnCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
        //Start the dialog and display it on screen.
        updateDialog.show()
    }*/


    private fun chooseNoteType() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_choose_new_note_type)

        customDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
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


            databaseHandler.addTextNote(TextNote(0, title, desc, LocalDateTime.now(),LocalDateTime.now()))
            setupListofDataIntoRecyclerView()
            Toast.makeText(this, "Added new note ${title}", Toast.LENGTH_SHORT).show()

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
                        Toast.makeText(view.context, "Dialog Radio: text", Toast.LENGTH_LONG).show()
                        addNewTextNoteAlertDialog()
                    }
                R.id.radio_photo ->
                    if (checked) {
                        Toast.makeText(view.context, "Dialog Radio: photo", Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this, PhotoNoteActivity::class.java)
                        this.startActivity(intent)
                    }
            }
        }
    }

}