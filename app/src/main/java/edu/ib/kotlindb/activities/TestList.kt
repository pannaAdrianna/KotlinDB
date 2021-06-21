package edu.ib.kotlindb.activities

import BasicAdapter
import DatabaseHandler
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ib.kotlindb.R
import edu.ib.kotlindb.database.EmpModelClass
import java.io.BufferedReader
import java.io.InputStreamReader


class TestList : AppCompatActivity() {

    internal lateinit var databaseHandler: DatabaseHandler
    internal lateinit var empList: ArrayList<EmpModelClass>
    internal lateinit var rvItemsList: RecyclerView
    internal var searchView: SearchView? = null
    val requestCSVcode = 1
    internal lateinit var myList: ArrayList<HashMap<String, String>>
    private var adapter: BasicAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_list)
//        setSupportActionBar(findViewById(R.id.toolbar))
        getSupportActionBar()?.setTitle("Basic Dangerous Ingredients")
        rvItemsList =  findViewById(R.id.rvItemsBasicList)
        searchView = findViewById(R.id.search_bar)
        setupListofDataIntoRecyclerView()
        adapter = BasicAdapter(this,getItemsList())
        rvItemsList.setAdapter(adapter);



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


    /**
     * Function is used to show the list on UI of inserted data.
     */
    private fun setupListofDataIntoRecyclerView() {
        rvItemsList = findViewById(R.id.rvItemsBasicList)
        val tvNoRecordsAvailable: TextView = findViewById(R.id.tvNoRecordsAvailable)

        if (getItemsList().size == 0) {
            readFromCSV()
        }

        rvItemsList.visibility = View.VISIBLE
        tvNoRecordsAvailable.visibility = View.GONE
        rvItemsList.layoutManager = LinearLayoutManager(this)
        val itemAdapter = BasicAdapter(this, getItemsList())
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
            databaseHandler.addEmployee(
                EmpModelClass(
                    i + 1,
                    row[0],
                    row[1]
                )
            )
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
        builder.setMessage("Are you sure you wants to delete ${empModelClass.label}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            databaseHandler = DatabaseHandler(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteEmployee(
                EmpModelClass(
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