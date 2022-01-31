package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Observer
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityProfileEditorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.random.Random

class ProfileEditor : AppCompatActivity() {
    lateinit var binding: ActivityProfileEditorBinding

    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var t: DatabaseReference
    lateinit var imageUri: Uri
    lateinit var imgUri: String
    var isClicked: Boolean = false

    lateinit var observer: Observer<ArrayList<AppUser>>
    var livaData: UserLive = UserLive()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.background = null

        val Email: String? = intent.getStringExtra("email")
        val Name1: String? = intent.getStringExtra("name1")
        val Name2: String? = intent.getStringExtra("name2")
        imgUri = intent.getStringExtra("uri")!!
        val pass: String? = intent.getStringExtra("pass")

        binding.email.setText(Email)
        binding.name.setText(Name1)
        binding.surname.setText(Name2)
        binding.pass.setText(pass)
        Glide.with(this)
            .load(imgUri)
            .into(binding.avatar)

        binding.pass.doOnTextChanged { text, start, before, count ->
            if(text?.length!! <= 7){
                binding.passL.error = "Не менее 8 символов"
            }else{
                binding.passL.error = null
            }
        }
        binding.button.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            t = auth.currentUser?.let { it1 -> FirebaseDatabase.getInstance().getReference(it1.uid) }!!
            t.removeValue()
            t = auth.uid?.let { it1 -> FirebaseDatabase.getInstance().getReference(it1) }!!



            val user = auth.currentUser
            user!!.updateEmail(binding.email.text.toString())
            user!!.updatePassword(binding.pass.text.toString())

            val i: Intent = Intent(this, MainActivity::class.java)
            startActivity(i)
            if(!isClicked){
                dontApploadNewImage()
            }else{
                uploadNewImage()
            }
        }

        binding.button2.setOnClickListener {
            isClicked = true
            pickNewImage()
        }


    }
    companion object{
        val IMAGE_PICK_CODE = 1
        val PERMISSION_CODE = 2
    }

    private fun pickNewImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            if(data != null){
                imageUri = data.data!!
                binding.avatar.setImageURI(imageUri)
            }else{
                dontApploadNewImage()
            }
        }else{
            dontApploadNewImage()
        }
    }

    private fun uploadNewImage(){
        storage = FirebaseStorage.getInstance()
        val formatter = SimpleDateFormat("yyyy_mm_dd_hh__mm__ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val ref = FirebaseStorage.getInstance().getReference("avatars/${fileName}")
        imgUri = ref.downloadUrl.toString()
        ref.putFile(imageUri)
        val appUser = AppUser(binding.name.text.toString(), binding.surname.text.toString(), binding.email.text.toString(), binding.pass.text.toString(), imgUri)


    }

    private fun dontApploadNewImage(){
        val appUser = AppUser(binding.name.text.toString(), binding.surname.text.toString(), binding.email.text.toString(), binding.pass.text.toString(), imgUri)
        t.push().setValue(appUser)
        finish()
    }



}