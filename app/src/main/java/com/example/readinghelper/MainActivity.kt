package com.example.readinghelper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.readinghelper.calendar.FragCalendar
import com.example.readinghelper.chatbot.FragHome
import com.example.readinghelper.databinding.ActivityMainBinding
import com.example.readinghelper.library.FragLibrary
import com.example.readinghelper.report.FragBoard
import com.example.readinghelper.search.FragSearch


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    private val fragHome: FragHome by lazy{ FragHome() }
    private val fragSearch: FragSearch by lazy { FragSearch() }
    private val fragCalendar: FragCalendar by lazy{ FragCalendar() }
    private val fragBoard: FragBoard by lazy{ FragBoard() }
    private val fragLibrary: FragLibrary by lazy{ FragLibrary() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

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
            selectedItemId=R.id.menu_home
        }

    }

    private fun changeFragment(fragment:Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_frame,fragment)
            .commit()
    }

}

