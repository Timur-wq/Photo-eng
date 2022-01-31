package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelStores
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.myapplication.UserModels.UserModel
import com.example.myapplication.databinding.ActivityProfileBinding
import com.example.myapplication.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

class Profile : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding

    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var table: DatabaseReference

    lateinit var imgUri: String
    lateinit var pass: String

    //lateinit var observer: Observer<ArrayList<AppUser>>
    val userLive = UserLive()
    lateinit var userModel: UserModel


    override fun onCreate(savedInstanceState: Bundle?) {
        userModel = ViewModelProviders.of(this).get(UserModel::class.java)
        storage = FirebaseStorage.getInstance()

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCancelable(false)
        dialog.show()

        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.background = null
        binding.bottomNavigation.selectedItemId = R.id.profile
        binding.avatar.clipToOutline = true
        var i: Intent

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    overridePendingTransition(0, 0)
                    finish()
                }
                R.id.settings -> {
                    i = Intent(this, Settings::class.java)
                    startActivity(i)
                    overridePendingTransition(0, 0)
                    finish()
                }
                R.id.extra->{
                    i = Intent(this, StartTest::class.java)
                    startActivity(i)
                    overridePendingTransition(0, 0)
                    finish()
                }
            }
            true
        }

        userModel.user.observe(this, {
            if(it != null) {
                binding.email.text = it.email
                binding.name.text = it.name
                binding.surname.text = it.name2
                imgUri = it.imgUri
                pass = it.password
                if(dialog.isShowing){
                    dialog.dismiss()
                }
                //val storageRef = storage.getReference().child("avatars/${it.imgUri}")
                //storageRef.getBytes(1024*1024).addOnSuccessListener {
                //    val bitmap = BitmapFactory.decodeByteArray(it, 0, (it.size).toInt())
                //    val bitmapScaled = Bitmap.createScaledBitmap(bitmap, (bitmap.width * 0.15625).toInt(),
                //        (bitmap.height * 0.15625).toInt(), false
                //    )
                //    binding.avatar.setImageBitmap(bitmapScaled)
//
//
                //}
                Glide.with(this)
                    .load(it.imgUri)
                    .into(binding.avatar)
            }
        })


        //readUserData()


        binding.button.setOnClickListener {
            i = Intent(this, ProfileEditor::class.java)
            i.putExtra("email", binding.email.text)
            i.putExtra("name1", binding.name.text)
            i.putExtra("name2", binding.surname.text)
            i.putExtra("pass", pass)
            i.putExtra("uri", imgUri)
            startActivity(i)
            finish()


        }


    }
    //var list = ArrayList<AppUser>()
    //fun readUserData(){
    //    val result = lifecycleScope.async {
    //        val db = Room.databaseBuilder(applicationContext, Database::class.java, "tunes").build()
    //        val res = db.playList().select1()
//
    //        launch(Dispatchers.Main) {
    //            val bytes = res?.get(0)?.photo
    //            val iStr: InputStream = ByteArrayInputStream(bytes)
    //            val o: BitmapFactory.Options = BitmapFactory.Options()
    //            val bitmap = BitmapFactory.decodeStream(iStr, null, o)
    //            binding.avatar.setImageBitmap(bitmap)
//
//
//
    //            //binding.pass.text = res?.get(0)?.photo.toString()
    //        }
    //    }


        /*auth = FirebaseAuth.getInstance()

        var intent1: Intent

        table = auth.currentUser?.let { FirebaseDatabase.getInstance().getReference(it.uid) }!!
        val vListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){
                    var model = i.getValue(AppUser::class.java)
                    list.add(model as AppUser)
                }
                binding.email.text = list.get(0).email
                binding.name.text = list.get(0).name
                binding.surname.text = list.get(0).name2

                storage = FirebaseStorage.getInstance()
                val storageRef = storage.getReference().child("avatars/${list.get(0).imgUri}")
                /*storageRef.getBytes(1024*1024).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, (it.size).toInt())
                    val bitmapScaled = Bitmap.createScaledBitmap(bitmap, (bitmap.width * 0.15625).toInt(),
                        (bitmap.height * 0.15625).toInt(), false
                    )
                    binding.avatar.setImageBitmap(bitmapScaled)


                }*/

                //storageRef.downloadUrl.addOnSuccessListener {
                //    Glide.with(applicationContext).load(it).into(binding.avatar)
                //}



            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        table.addValueEventListener(vListener)*/



}

