package com.example.myapplication.UserModels

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.R
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class RecyclerViewModel(app: Application): AndroidViewModel(app) {
    var translatorData: MutableLiveData<MutableList<MutableList<String>>>
    lateinit var table: DatabaseReference
    lateinit var auth: FirebaseAuth
    var lablesLi = mutableListOf<MutableList<String>>()

    init {
        translatorData = MutableLiveData()
        loadData()
    }

    fun loadData(){
        auth = FirebaseAuth.getInstance()
        table = FirebaseDatabase.getInstance().getReference(auth?.uid!!).child("Dict")
        val vListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    var model = i.getValue(Dictonary::class.java)
                    model?.wordList?.let { lablesLi.add(it) }
                }
                translatorData.postValue(lablesLi)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        table.addValueEventListener(vListener)
    }
}