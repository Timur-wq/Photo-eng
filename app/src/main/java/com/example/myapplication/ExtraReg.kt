package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.example.myapplication.databinding.ActivityExtraRegBinding

class ExtraReg : AppCompatActivity() {
    lateinit var binding: ActivityExtraRegBinding
    var profSphere: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityExtraRegBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tick.setColorFilter(Color.argb(255, 255, 255, 255))

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.student -> {
                    profSphere = 0
                }
                R.id.carrier -> {
                    profSphere = 1
                }
            }
        }

        binding.tick.setOnClickListener {
            if (profSphere == 0){
                val sharedPrefs = getSharedPreferences("profs", Context.MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor.apply{
                    putInt("Prof", 6)
                }.apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this, ExtraReg2::class.java)
                intent.putExtra("profSphere", profSphere)
                startActivity(intent)
                finish()
            }
        }
    }
}