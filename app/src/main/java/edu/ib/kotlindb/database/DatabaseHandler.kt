import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.BitmapFactory
import edu.ib.kotlindb.models.Note
import edu.ib.kotlindb.models.PhotoNote
import edu.ib.kotlindb.models.TextNote
import java.time.LocalDateTime

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
        private val DATABASE_VERSION = 7
        private val DATABASE_NAME = "NotesDatabase"


        private val TABLE_TEXT_NOTES = "TextNotesTable"
        private val TABLE_PHOTO_NOTES = "PhotoNotesTable"

        private val KEY_ID = "_id"


        private val KEY_TITLE = "title"
        private val KEY_DESC = "desc"
        private val KEY_IMAGE = "image"
        private val KEY_CREATED_DATE = "createdAt"
        private val KEY_MODIFICATION_DATE = "modificationDate"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields


        val CREATE_TEXTNOTES_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_TEXT_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_DESC + " TEXT," + KEY_CREATED_DATE + " TEXT," + KEY_MODIFICATION_DATE + " TEXT" + ")")
        db?.execSQL(CREATE_TEXTNOTES_TABLE)

        val CREATE_PHOTONOTES_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_PHOTO_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_CREATED_DATE + " TEXT,"
                + KEY_IMAGE + " BLOB" + ")")
        db?.execSQL(CREATE_PHOTONOTES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TEXT_NOTES")
        onCreate(db)
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_PHOTO_NOTES")
        onCreate(db)
    }


    fun addPhotoNote(item: PhotoNote): Long {


        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(KEY_TITLE, item.title)
        cv.put(KEY_IMAGE, item.image)
        val success = db.insert(TABLE_PHOTO_NOTES, null, cv)
        db.close()

        return success


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
                title = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                var imgByte = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE))
                BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size)

                val emp = PhotoNote(
                    id = id,
                    title = title,
                    desc = "desc",
                    image = imgByte,
                    createdAt = LocalDateTime.now(),
                    modificationDate = LocalDateTime.now(),
                )
                images.add(emp)

            } while (cursor.moveToNext())
        }

        println("\n\n\nIMAGES\n\n\n")
        println(images)
        return images


    }


    fun addTextNote(note: TextNote): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, note.title) // EmpModelClass Name
        contentValues.put(KEY_DESC, note.desc) // EmpModelClass Email
        contentValues.put(KEY_CREATED_DATE, note.createdAt.toString()) // EmpModelClass Email
        contentValues.put(
            KEY_MODIFICATION_DATE,
            note.modificationDate.toString()
        ) // EmpModelClass Email

        // Inserting employee details using insert query.
        val success = db.insert(TABLE_TEXT_NOTES, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }


    fun viewNotes(): ArrayList<Note> {

        val empList: ArrayList<Note> = ArrayList<Note>()

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
        var date: LocalDateTime
        var mod: LocalDateTime


        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                title = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                date =
                    LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(KEY_CREATED_DATE)))
                mod =
                    LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(KEY_MODIFICATION_DATE)))


                val note = Note(
                    id = id,
                    title = title,
                    createdAt = date,
                    modificationDate = mod
                )
                empList.add(note)

            } while (cursor.moveToNext())
        }
        return empList
    }


    fun updateTextNote(note: TextNote): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, note.title)
        contentValues.put(KEY_DESC, note.desc)
        contentValues.put(KEY_MODIFICATION_DATE, note.modificationDate.toString())

        val success = db.update(TABLE_TEXT_NOTES, contentValues, KEY_ID + "=" + note.id, null)
        db.close()
        return success
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
        var createdAt: String
        var mod: String


        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                title = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                desc = cursor.getString(cursor.getColumnIndex(KEY_DESC))
                createdAt = (cursor.getString(cursor.getColumnIndex(KEY_CREATED_DATE)))
                mod = (cursor.getString(cursor.getColumnIndex(KEY_MODIFICATION_DATE)))

                val emp = TextNote(
                    id = id,
                    title = title,
                    desc = desc,
                    createdAt = LocalDateTime.parse(createdAt),
                    modificationDate = LocalDateTime.parse(mod)
                )
                empList.add(emp)

            } while (cursor.moveToNext())
        }
        return empList
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


}