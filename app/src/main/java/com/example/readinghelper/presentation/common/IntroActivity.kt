package com.example.readinghelper.presentation.common

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.readinghelper.R
import com.example.readinghelper.data.model.User
import com.example.readinghelper.presentation.user.AuthActivity
import com.example.readinghelper.presentation.user.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : AppCompatActivity() , FirebaseAuth.AuthStateListener {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var authViewModel: AuthViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        val handler = Handler()
        handler.postDelayed({
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                val intent = Intent(this@IntroActivity, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this@IntroActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            }
        , 2000) //2초 뒤에 Runner객체 실행하도록 함
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            val intent = Intent(this@IntroActivity, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(this@IntroActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
