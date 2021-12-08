package edu.ib.kotlindb.activities

import DatabaseHandler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.fragment.app.DialogFragment
import edu.ib.kotlindb.R


import edu.ib.kotlindb.models.TextNote
import java.time.LocalDateTime


class AddNewNoteDialog : DialogFragment() {
    internal lateinit var databaseHandler: DatabaseHandler


    lateinit var title: EditText
    lateinit var desc: EditText
    lateinit var add_btn: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);


        return inflater.inflate(R.layout.dialog_add_new_text_note, container, false)
    }

    override fun onStart() {
        super.onStart()



        title = view?.findViewById(R.id.et_title)!!;
        desc = view?.findViewById(R.id.et_desc)!!;


        add_btn = view?.findViewById(R.id.btn_add)!!;
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        add_btn.setOnClickListener {
            readFromInputs()
        }
        dialog!!.setCanceledOnTouchOutside(true);

    }


    private fun clearForm() {
        title.text.clear();
        desc.text.clear();
    }

    fun createNote(): TextNote {
        return TextNote(0, title.text.toString(), desc.text.toString(), LocalDateTime.now())
    }


    private fun readFromInputs() {

//        Toast.makeText(context, "Add button click in dialog fragment", Toast.LENGTH_LONG).show()
        Toast.makeText(context, title.text, Toast.LENGTH_SHORT).show()

        var new_note = createNote()
        println("Add new note dialog: $new_note")
        clearForm()

    }
}