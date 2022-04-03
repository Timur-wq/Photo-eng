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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ExtraReg2 : AppCompatActivity() {

    lateinit var binding: ActivityExtraReg2Binding

    lateinit var table: DatabaseReference
    lateinit var auth: FirebaseAuth
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
            auth = FirebaseAuth.getInstance()
            table = auth.currentUser?.let { it1 -> FirebaseDatabase.getInstance().getReference(it1.uid).child("BasisType") }!!
            val basisType = BasisType(profSphere)
            table.push().setValue(basisType)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}