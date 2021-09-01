package com.example.readinghelper.presentation.user

import com.example.readinghelper.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.readinghelper.data.model.User
import com.example.readinghelper.presentation.common.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider


class AuthActivity : AppCompatActivity() {
    private var authViewModel: AuthViewModel? = null
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val googleSignInButton = findViewById<SignInButton>(R.id.sign_in_button)
        googleSignInButton.setOnClickListener { v: View? -> signIn() }
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        initGoogleSignIn()
    }

    private fun initGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, 123)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                if (googleSignInAccount != null) {
                    getGoogleAuth(googleSignInAccount)
                }
            } catch (e: ApiException) {
            }
        }
    }

    private fun getGoogleAuth(googleAccount: GoogleSignInAccount) {
        val googleTokenId = googleAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogle(googleAuthCredential)
    }

    private fun signInWithGoogle(googleAuth: AuthCredential) {
        authViewModel!!.signInWithGoogle(googleAuth)
        authViewModel!!.authUserLiveData!!.observe(this, { user: User ->
            // 새로운 유저일 때: 프로필 등록
            if (user.isNew) {
                goToProfileActivity(user)
            } else {
                goToMainActivity(user)
            }
        })
    }

    private fun toastMessage(name: String) {
        Toast.makeText(this, "Hi $name!\nYour account was successfully created.", Toast.LENGTH_LONG).show()
    }

    private fun goToProfileActivity(user: User) {
        val intent = Intent(this@AuthActivity, ProfileActivity::class.java)
        intent.putExtra("users", user)
        System.out.println("ssssprofile" + user.name)
        startActivity(intent)
        finish()
    }

    private fun goToMainActivity(user: User) {
        val intent = Intent(this@AuthActivity, MainActivity::class.java)
        intent.putExtra("users", user)
        System.out.println("ssssauthact" + user.name)
        startActivity(intent)
        finish()
    }
}