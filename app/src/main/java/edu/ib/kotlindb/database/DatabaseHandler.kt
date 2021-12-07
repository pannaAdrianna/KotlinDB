import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.BitmapFactory
import edu.ib.kotlindb.database.EmpModelClass
import edu.ib.kotlindb.models.PhotoNote
import edu.ib.kotlindb.models.TextNote

/**
 *
 * - companion object
 * - null check
 */
//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //    companion object
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "NotesDatabase"

        private val TABLE_CONTACTS = "EmployeeTable"
        private val TABLE_PREGGO = "PreggoTable"
        private val TABLE = "PregTable"
        private val TABLE_TEXT_NOTES = "TextNotesTable"
        private val TABLE_PHOTO_NOTES = "PhotoNotesTable"

        private val KEY_ID = "_id"

        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val KEY_TITLE = "title"
        private val KEY_DESC = "desc"
        private val KEY_IMAGE = "image"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)

        val CREATE_PREGGO_TABLE = ("CREATE TABLE " + TABLE_PREGGO + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")")
        db?.execSQL(CREATE_PREGGO_TABLE)

        val CREATE_PREGGO2_TABLE = ("CREATE TABLE " + TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")")
        db?.execSQL(CREATE_PREGGO2_TABLE)


        val CREATE_TEXTNOTES_TABLE = ("CREATE TABLE " + TABLE_TEXT_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_DESC + " TEXT" + ")")
        db?.execSQL(CREATE_TEXTNOTES_TABLE)

        val CREATE_PHOTONOTES_TABLE = ("CREATE TABLE " + TABLE_PHOTO_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " BLOB" + ")")
        db?.execSQL(CREATE_PHOTONOTES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)

        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_PREGGO")
        onCreate(db)

        db!!.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)

        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TEXT_NOTES")
        onCreate(db)
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_PHOTO_NOTES")
        onCreate(db)
    }


    fun addPhotoNote(name: String?, image: ByteArray) {
        try {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(KEY_NAME, name)
            cv.put(KEY_IMAGE, image)
            db.insert(TABLE_PHOTO_NOTES, null, cv)
        } catch (e: SQLiteException) {
            println(e)
        }

    }

    fun getImages(): ArrayList<PhotoNote> {
        val images: ArrayList<PhotoNote> = ArrayList<PhotoNote>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_PHOTO_NOTES"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var title: String
        var image: ByteArray
        var description: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                title = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                var imgByte = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE))
                BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size)

                val emp = PhotoNote(
                    id = id,
                    title = title,
                    desc = "desc",
                    image = imgByte
                )
                images.add(emp)

            } while (cursor.moveToNext())
        }

        println("\n\n\nIMAGES\n\n\n")
        println(images)
        return images


    }


    /**
     * Function to insert data
     */
    fun addEmployee(emp: EmpModelClass): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.label) // EmpModelClass Name
        contentValues.put(KEY_EMAIL, emp.description) // EmpModelClass Email

        // Inserting employee details using insert query.
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    /**
     * Function to insert data
     */
    fun addIngredient(emp: EmpModelClass): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.label) // EmpModelClass Name
        contentValues.put(KEY_EMAIL, emp.description) // EmpModelClass Email

        // Inserting employee details using insert query.
        val success = db.insert(TABLE_PREGGO, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    fun addPreggoIngredient(emp: EmpModelClass): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.label) // EmpModelClass Name
        contentValues.put(KEY_EMAIL, emp.description) // EmpModelClass Email

        // Inserting employee details using insert query.
        val success = db.insert(TABLE, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }


    fun addTextNote(emp: TextNote): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, emp.title) // EmpModelClass Name
        contentValues.put(KEY_DESC, emp.desc) // EmpModelClass Email

        // Inserting employee details using insert query.
        val success = db.insert(TABLE_TEXT_NOTES, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }


    //Method to read the records from database in form of ArrayList
    fun viewEmployee(): ArrayList<EmpModelClass> {

        val empList: ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var email: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))

                val emp = EmpModelClass(
                    id = id,
                    label = name,
                    description = email
                )
                empList.add(emp)

            } while (cursor.moveToNext())
        }
        return empList
    }

    fun viewPreggoIngredient(): ArrayList<EmpModelClass> {

        val empList: ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT * FROM $TABLE"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var email: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))

                val emp = EmpModelClass(
                    id = id,
                    label = name,
                    description = email
                )
                empList.add(emp)

            } while (cursor.moveToNext())
        }
        return empList
    }

    //Method to read the records from database in form of ArrayList
    fun viewIngredient(): ArrayList<EmpModelClass> {

        val empList: ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_PREGGO"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var email: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))

                val emp = EmpModelClass(
                    id = id,
                    label = name,
                    description = email
                )
                empList.add(emp)

            } while (cursor.moveToNext())
        }
        return empList
    }


    //Method to read the records from database in form of ArrayList
    fun viewTextNote(): ArrayList<TextNote> {

        val empList: ArrayList<TextNote> = ArrayList<TextNote>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_TEXT_NOTES"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var title: String
        var desc: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                title = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                desc = cursor.getString(cursor.getColumnIndex(KEY_DESC))

                val emp = TextNote(
                    id = id,
                    title = title,
                    desc = desc
                )
                empList.add(emp)

            } while (cursor.moveToNext())
        }
        return empList
    }


    /**
     * Function to delete record
     */
    fun deleteEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id) // EmpModelClass id
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + emp.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }


    /**
     * Function to delete record
     */
    fun deleteTextNote(emp: TextNote): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id) // EmpModelClass id
        // Deleting Row
        val success = db.delete(TABLE_TEXT_NOTES, KEY_ID + "=" + emp.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }


    /**
     * Function to delete record
     */
    fun deleteIngredient(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id) // EmpModelClass id
        // Deleting Row
        val success = db.delete(TABLE_PREGGO, KEY_ID + "=" + emp.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }


    fun getAllIngredients(): java.util.ArrayList<HashMap<String, String>> {
        val proList: java.util.ArrayList<HashMap<String, String>>
        proList = java.util.ArrayList()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                //Id, Company,Name,Price
                val map = HashMap<String, String>()
                map["_id"] = cursor.getString(0)
                map["name"] = cursor.getString(1)
                map["email"] = cursor.getString(2)
                proList.add(map)
            } while (cursor.moveToNext())
        }

        return proList
    }
}