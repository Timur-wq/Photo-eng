package com.example.myapplication

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class UserLive: LiveData<ArrayList<AppUser>>() {
    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var table: DatabaseReference
    var list = ArrayList<AppUser>()

    override fun onActive() {
        super.onActive()
        auth = FirebaseAuth.getInstance()
        table = auth.currentUser?.let { FirebaseDatabase.getInstance().getReference(it?.uid) }!!
        val vListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    var model = i.getValue(AppUser::class.java)
                    list.add(model as AppUser)
                }
                value = list
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        table.addValueEventListener(vListener)
    }

    override fun onInactive() {
        super.onInactive()
    }
}