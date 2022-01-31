package com.example.myapplication.UserModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myapplication.AppUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class UserModel(app: Application): AndroidViewModel(app){
    var user: MutableLiveData<AppUser>
    lateinit var auth: FirebaseAuth
    lateinit var table: DatabaseReference
    init {
        user = MutableLiveData()
        loadData()
    }

    fun loadData(){
        auth = FirebaseAuth.getInstance()
        table = auth.currentUser?.let { FirebaseDatabase.getInstance().getReference(it?.uid) }!!
        val vListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    var model = i.getValue(AppUser::class.java)
                    user.postValue(model)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        table.addValueEventListener(vListener)
    }
}