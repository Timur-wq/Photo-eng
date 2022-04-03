package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.example.myapplication.databinding.ActivityExtraRegBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ExtraReg : AppCompatActivity() {

    lateinit var table: DatabaseReference
    lateinit var auth: FirebaseAuth

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

            auth = FirebaseAuth.getInstance()
            table = auth.currentUser?.let { it1 -> FirebaseDatabase.getInstance().getReference(it1.uid).child("BasisType") }!!
            if (profSphere == 0){
                val basisType = BasisType(6)
                table.push().setValue(basisType)
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