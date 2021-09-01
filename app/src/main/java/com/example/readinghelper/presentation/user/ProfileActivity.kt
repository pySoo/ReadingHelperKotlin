package com.example.readinghelper.presentation.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.readinghelper.R
import com.example.readinghelper.data.model.User
import com.example.readinghelper.presentation.common.MainActivity

class ProfileActivity : AppCompatActivity() {
    private var authViewModel: AuthViewModel? = null
    private var subButton: Button? = null
    private var nameText: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val user: User? = intent.getParcelableExtra<User>("users")
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        nameText = findViewById(R.id.nameEditText) as EditText
        subButton = findViewById(R.id.subButton)
        subButton!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                // 이름 필수 입력
                if (nameText!!.text.toString().length == 0) {
                    toastMessage("이름을 입력하세요.")
                } else {
                    updateProfile(user)
                }
            }

        })
    }

    private fun updateProfile(authenticatedUser:User?){
        val name = nameText?.text.toString()
        if (authenticatedUser != null) {
            authenticatedUser.name = name
        }
        authViewModel!!.createUser(authenticatedUser)
        authViewModel!!.createdUserLiveData!!.observe(this, { user: User ->
            if (user.isCreated) {
                toastMessage(user.name)
                goToMainActivity(user)
            }

        })

    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun goToMainActivity(user: User) {
        val intent = Intent(this@ProfileActivity, MainActivity::class.java)
        intent.putExtra("users", user)
        System.out.println("ssssauthact" + user.name)
        startActivity(intent)
        finish()
    }

}