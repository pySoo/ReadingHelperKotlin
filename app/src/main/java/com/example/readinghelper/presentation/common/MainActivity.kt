package com.example.readinghelper.presentation.common

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.readinghelper.R
import com.example.readinghelper.data.model.User
import com.example.readinghelper.databinding.ActivityMainBinding
import com.example.readinghelper.presentation.calendar.FragCalendar
import com.example.readinghelper.presentation.chatbot.FragHome
import com.example.readinghelper.presentation.library.FragLibrary
import com.example.readinghelper.presentation.report.FragBoard
import com.example.readinghelper.presentation.search.FragSearch
import com.example.readinghelper.presentation.user.AuthActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import java.sql.DriverManager.println


class MainActivity : AppCompatActivity(), AuthStateListener {
    private lateinit var binding:ActivityMainBinding

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null
    private var messageTextView: TextView? = null

    private val fragHome: FragHome by lazy{ FragHome() }
    private val fragSearch: FragSearch by lazy { FragSearch() }
    private val fragCalendar: FragCalendar by lazy{ FragCalendar() }
    private val fragBoard: FragBoard by lazy{ FragBoard() }
    private val fragLibrary: FragLibrary by lazy{ FragLibrary() }

    private var logoutBtn : Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // 유저 정보 얻기
        val user: User? = intent.getParcelableExtra<User>("users")
        System.out.println("ssssmainuser: " + user.toString())
        initGoogleSignIn()
        initMessageTextView(user)

        // 로그아웃 버튼 누른 후 처리
        logoutBtn = findViewById(R.id.logout_button) as Button
        logoutBtn!!.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                signOut()
            }

        })
        //binding.pageInfo.setText("메인페이지 바꿨다잉")
        binding.bottomNavi.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.menu_home -> {
                        changeFragment(fragHome)
                    }
                    R.id.menu_search -> {
                        changeFragment(fragSearch)
                    }
                    R.id.menu_calendar -> {
                        changeFragment(fragCalendar)
                    }
                    R.id.menu_board -> {
                        changeFragment(fragBoard)
                    }
                    R.id.menu_library -> {
                        changeFragment(fragLibrary)
                    }

                }
                true
            }
            selectedItemId= R.id.menu_home
        }

    }
    private fun changeFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_frame, fragment)
            .commit()
    }

    // 구글 초기화
    private fun initGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun initMessageTextView(user: User?) {
        messageTextView = findViewById(R.id.message_text_view)
        val message = (user?.name ?: "이름 없음") + "님 환영합니다!"
        messageTextView!!.text = message
    }

    // 로그아웃 되면 로그인 페이지(AuthActivity)로 이동
    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            val intent = Intent(this@MainActivity, AuthActivity::class.java)
            startActivity(intent)
        }
    }

    // 구글, 파이어 베이스 로그아웃
    private fun signOut() {
        firebaseAuth.signOut()
        googleSignInClient!!.signOut()
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(this)
    }

}

