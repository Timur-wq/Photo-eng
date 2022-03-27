package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var table: DatabaseReference
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.background = null
        var i: Intent
        binding.bottomNavigation.selectedItemId = R.id.home
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.settings ->{
                    i = Intent(this, Settings::class.java)
                    startActivity(i)
                    overridePendingTransition(0, 0)
                    finish()
                }
                R.id.extra ->{
                    i = Intent(this, StartTest::class.java)
                    startActivity(i)
                    overridePendingTransition(0, 0)
                    finish()
                }
            }

            true
        }

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.vp2.adapter = adapter

        val lang = Locale.getDefault().language
        TabLayoutMediator(binding.tl, binding.vp2){tab, position ->
            when(position){
                0 ->{
                    if(lang.equals("ru")) {
                        tab.text = "Добавить"
                    }else if(lang.equals("be")){
                        tab.text = "Дадаць"
                    }else if(lang.equals("uk")){
                        tab.text = "Додати"
                    }
                }
                1 -> {
                    if(lang.equals("ru")) {
                        tab.text = "Словарь"
                    }else if(lang.equals("be")){
                        tab.text = "Слоўнік"
                    }else if(lang.equals("uk")){
                        tab.text = "Словник"
                    }
                }
                2->{
                    if(lang.equals("ru")) {
                        tab.text = "Тестирование"
                    }else if(lang.equals("be")){
                        tab.text = "Тэсціраванне"
                    }else if(lang.equals("uk")){
                        tab.text = "Тестування"
                    }
                }
            }
        }.attach()
    }
}