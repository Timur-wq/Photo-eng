package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import com.example.myapplication.databinding.ActivityExtraReg2Binding

class ExtraReg2 : AppCompatActivity() {

    lateinit var binding: ActivityExtraReg2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtraReg2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        var profSphere = intent.getIntExtra("profSphere", 0)

        binding.tick.setColorFilter(Color.argb(255, 255, 255, 255))

        binding.langSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?){

            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, itemId: Long){
                //Toast.makeText(applicationContext, "${adapterView?.getItemAtPosition(position).toString()}", Toast.LENGTH_SHORT).show()
                profSphere = position
            }
        }

        binding.tick.setOnClickListener {
            val sharedPrefs = getSharedPreferences("profs", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.apply{
                putInt("Prof", profSphere)
            }.apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}