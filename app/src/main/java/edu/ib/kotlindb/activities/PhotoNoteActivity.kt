package edu.ib.kotlindb.activities

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import edu.ib.kotlindb.R

class PhotoNoteActivity : AppCompatActivity() {



    companion object {
        var mResultEt: EditText? = null
        var mPreviewIv: ImageView? = null
        var MESSAGE = "MESSAGE" //used as id to another activity
        private const val CAMERA_REQUEST_CODE = 200
        private const val STORAGE_REQUEST_CODE = 400
        private const val IMAGE_PICK_GALLERY_CODE = 1000
        private const val IMAGE_PICK_CAMERA_CODE = 1001


        lateinit var cameraPermission: Array<String>
        lateinit var storagePermission: Array<String>

        var image_uri: Uri? = null

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_note)

        mResultEt = findViewById(R.id.resultEt)
        mPreviewIv = findViewById(R.id.ivImage)

        val actionBar = supportActionBar
        actionBar!!.subtitle = "Click + button to insert Image"

        //canera permission
        //canera permission
        cameraPermission = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        //storage permission
        //storage permission
        storagePermission = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    fun onBtnAnalyzeClick(view: View) {
        val intent = Intent(this, AnalyzeActivity::class.java)
        intent.putExtra(PhotoNoteActivity.MESSAGE, mResultEt?.text.toString())
        startActivity(intent)

    }

    /**
     * check if there is a menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_photo_notes, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.addImage) {
            showImageImportDialog()
        }
        if (id == R.id.settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * method asks for permission of the camera, storage (if there isn't) or open camera/storage if there is
     */
    private fun showImageImportDialog() {
        val items = arrayOf(" Camera", " Gallery")
        val dialog =
            AlertDialog.Builder(this)
        dialog.setTitle("Select Image")
        dialog.setItems(
            items
        ) { dialog1: DialogInterface?, which: Int ->
            if (which == 0) {
                //camera option clicked
                // ask for a permission
                if (!checkCameraPermission()) {
                    requestCameraPermission()
                } else {
                    pickCamera()
                }
            }
            if (which == 1) {
                //gallery
                if (!checkStoragePermission()) {
                    requestStoragePermission()
                } else {
                    pickGallery()
                }
            }
        }
        dialog.create().show()
    }


    /**
     * intent to pick image from the gallery
     */
    private fun pickGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PhotoNoteActivity.IMAGE_PICK_GALLERY_CODE)
    }

    /**
     * method asks for permission to storage
     */
    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, PhotoNoteActivity.STORAGE_REQUEST_CODE)
    }

    /**
     * method asks if there is permission to write files
     * @return true if there is active permission
     */
    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * method turn on camera
     */
    private fun pickCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "OCRpicture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To text") //
        image_uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, PhotoNoteActivity.IMAGE_PICK_CAMERA_CODE)
    }

    /**
     * method asks for a permission to turn on camera
     */
    private fun requestCameraPermission() {
        // can run the camera and save to gallery
        ActivityCompat.requestPermissions(this, cameraPermission, PhotoNoteActivity.CAMERA_REQUEST_CODE)
    }

    /**
     * method asks if there is permission to write files (save image) and open camera
     * @return true if there is active permission to open camera and write files
     */
    private fun checkCameraPermission(): Boolean {
        val resultCamera = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val resultWrite = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        return resultCamera && resultWrite
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> if (grantResults.size > 0) {
                val cameraAccepted =
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                val writeStorageAccepted =
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (cameraAccepted && writeStorageAccepted) {
                    pickCamera()
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
            }
            STORAGE_REQUEST_CODE -> if (grantResults.size > 0) {
                val writeStorageAccepted =
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (writeStorageAccepted) {
                    pickGallery()
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //crop
                CropImage.activity(data!!.data).setGuidelines(CropImageView.Guidelines.ON)
                    .start(this) //enable image guidlines
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON)
                    .start(this) //enable image guidlines
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri // get image uri

                // setting image to ImV
                mPreviewIv!!.setImageURI(resultUri)

                // drawable bitmap for text recogni
                val bitmapDrawable = mPreviewIv!!.drawable as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap
                val recognizer =
                    TextRecognizer.Builder(applicationContext).build()
                if (!recognizer.isOperational) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                } else {
                    val frame =
                        Frame.Builder().setBitmap(bitmap).build()
                    val items = recognizer.detect(frame)
                    val sb = StringBuilder()
                    //geting text from sb to the end
                    for (i in 0 until items.size()) {
                        val myItem = items.valueAt(i)
                        sb.append(myItem.value)
                        sb.append("\n")
                    }
                    //set text to edit text
                    mResultEt!!.setText(sb.toString())
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //secure if there is any error. Show
                val e = result.error
                Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show()
            }
        }
    }

}