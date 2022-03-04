package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityStartTestBinding

class StartTest : AppCompatActivity() {
    lateinit var binding: ActivityStartTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityStartTestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomNavigation.background = null
        var i: Intent
        binding.bottomNavigation.selectedItemId = R.id.extra
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.settings ->{
                    i = Intent(this, Settings::class.java)
                    startActivity(i)
                    overridePendingTransition(0, 0)
                    finish()
                }
                R.id.home->{
                    i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    overridePendingTransition(0, 0)
                    finish()
                }
            }

            true
        }

        binding.button3.setOnClickListener{
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }
    }
}