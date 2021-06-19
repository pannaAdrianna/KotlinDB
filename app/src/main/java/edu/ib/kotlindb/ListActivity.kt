package edu.ib.kotlindb

import DatabaseHandler
import ItemAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.InputStreamReader


class ListActivity : AppCompatActivity() {
    internal lateinit var databaseHandler: DatabaseHandler
    internal lateinit var empList: ArrayList<EmpModelClass>
    internal lateinit var rvItemsList: RecyclerView
    val requestCSVcode = 1
    internal lateinit var myList: ArrayList<HashMap<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
//        setSupportActionBar(findViewById(R.id.toolbar))
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        // Click even of the add button.
        btnAdd.setOnClickListener { view ->

            addRecord()
        }
        setupListofDataIntoRecyclerView()


    }

    /**
     * Function is used to get the Items List from the database table.
     */
    private fun getItemsList(): ArrayList<EmpModelClass> {
        //creating the instance of DatabaseHandler class
        databaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        empList = databaseHandler.viewEmployee()

        return empList
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Method for saving the employee records in database
    private fun addRecord() {
        val etName: EditText = findViewById(R.id.etName)
        val etEmailId: EditText = findViewById(R.id.etEmailId)

        val name = etName.text.toString()
        val email = etEmailId.text.toString()
        databaseHandler = DatabaseHandler(this)
        if (!name.isEmpty() && !email.isEmpty()) {
            val status =
                databaseHandler.addEmployee(EmpModelClass(0, name, email))
            if (status > -1) {
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                etName.text.clear()
                etEmailId.text.clear()

                setupListofDataIntoRecyclerView()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Label of Description cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Function is used to show the list on UI of inserted data.
     */
    private fun setupListofDataIntoRecyclerView() {
        rvItemsList = findViewById(R.id.rvItemsList)
        val tvNoRecordsAvailable: TextView = findViewById(R.id.tvNoRecordsAvailable)

        if (getItemsList().size == 0) {
            readFromCSV()
        }

        rvItemsList.visibility = View.VISIBLE
        tvNoRecordsAvailable.visibility = View.GONE
        rvItemsList.layoutManager = LinearLayoutManager(this)
        val itemAdapter = ItemAdapter(this, getItemsList())
        rvItemsList.adapter = itemAdapter




    }


    fun readFromCSV() {


        val minput = InputStreamReader(assets.open("data.csv"))
        val reader = BufferedReader(minput)

        var line: String
        var displayData: String = ""

        var i: Int = 0
        while (reader.readLine().also { line = it } != null) {
            i += 1
            val row: List<String> = line.split(';')
            displayData = displayData + row[0] + "\t" + row[1] + "\n"
            databaseHandler.addEmployee(EmpModelClass(i + 1, row[0], row[1]))
        }


    }


    /**
     * Method is used to show the Alert Dialog.
     */
    fun deleteRecordAlertDialog(empModelClass: EmpModelClass) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${empModelClass.name}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            databaseHandler = DatabaseHandler(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteEmployee(EmpModelClass(empModelClass.id, "", ""))
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