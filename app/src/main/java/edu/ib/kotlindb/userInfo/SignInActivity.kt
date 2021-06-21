package edu.ib.kotlindb.userInfo


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import edu.ib.kotlindb.activities.MainActivity
import edu.ib.kotlindb.R

class SignInActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        getSupportActionBar()?.hide()

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        Handler().postDelayed({
            if (user != null) {
                // gdy zalogowany
                val mainActivity = Intent(this, MainActivity::class.java)
                startActivity(mainActivity)
            } else {
                val signInIntent = Intent(this, SignUpActivity::class.java)
                startActivity(signInIntent)
            }

        }, 2000)
    }
}