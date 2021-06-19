package edu.ib.kotlindb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class AccountDashboard : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_dashboard)
        auth = FirebaseAuth.getInstance()
        var user = auth.currentUser

        if (user == null) {
            println("Użytkownik pusty - on create")
        } else {
            user?.let {
                val name: TextView? = findViewById(R.id.name_txt)
                val email: TextView? = findViewById(R.id.email_txt)
                val image: ImageView? = findViewById(R.id.profile_image)
                // Name, email address, and profile photo Url
                val nameFB = user.displayName
                val emailFB = user.email
                val photoUrlFB = user.photoUrl

                // Check if user's email is verified
                val emailVerifiedFB = user.isEmailVerified

                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getToken() instead.
                val uidFB = user.uid

                name?.setText(user?.displayName)
                email?.setText(user?.email)
                if (image != null) {
                    Glide.with(this).load(user?.photoUrl).into(image)
                }

            }


        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
//            reload();
            println("Użytkownik pusty On start")
        }
    }

    fun onBtnSignOutClick(view: View) {
        FirebaseAuth.getInstance().signOut();
        val intent = Intent(this, SignInActivity::class.java)
        this.startActivity(intent)

    }
    fun onBtnPersonalizeClick(view: View) {

        val intent = Intent(this, PersonalizedList::class.java)
        this.startActivity(intent)

    }


}