package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.myapplication.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class Registration : AppCompatActivity() {
    //database
    lateinit var table: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var imageUri: Uri

    lateinit var downloadUri: String

    lateinit var binding: ActivityRegistrationBinding
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.clipToOutline = true
        binding.button4.setOnClickListener {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    pickImage()
                }
            }else{
                pickImage()
            }
        }
        initDB()

        binding.pass1.doOnTextChanged { text, start, before, count ->
            if(text?.length!! <= 7){
                binding.passL.error = resources.getString(R.string.pass_rule)
            }else{
                binding.passL.error = null
            }
        }
        //binding.button.visibility = View.INVISIBLE



        binding.button.setOnClickListener {

            val user = auth.currentUser
            user?.sendEmailVerification()
            binding.button.visibility = View.VISIBLE
            //send verification link
            val email: String = binding.email.text.toString()
            val password: String = binding.pass1.text.toString()
            val name1: String = binding.name1.text.toString()
            val name2: String = binding.name2.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty() && name1.isNotEmpty() && name2.isNotEmpty() && imageUri != null){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                            val toast = Toast.makeText(applicationContext, "Подтведите email", Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.CENTER, 0, 0)
                            toast.show()
                        }
                        val i = Intent(this, Authorisation::class.java)
                        uploadImage()
                        startActivity(i)
                    }
                }
            }else{
                val toast = Toast.makeText(applicationContext, resources.getString(R.string.fill_fields), Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }


        }
    }

    private fun initDB(){
        auth = FirebaseAuth.getInstance()
        table = FirebaseDatabase.getInstance().getReference()
    }
    lateinit var fileName: String

    private fun uploadImage(){
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        fileName = formatter.format(now)

        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val ref = storage.getReference("avatars/${fileName}")


        ref.putFile(imageUri)
        /*val task = up.continueWithTask{task ->
            if(!task.isSuccessful){}
            ref.downloadUrl
        }.addOnCompleteListener { task->
            if(task.isSuccessful){
                downloadUri = task.result.toString()

            }
        }*/
        putData()


    }
    private fun putData(){
        val tableRef = auth.currentUser?.let { FirebaseDatabase.getInstance().getReference(it.uid) }
        val appUser = AppUser(binding.name1.text.toString(), binding.name2.text.toString(), binding.email.text.toString(), binding.pass1.text.toString(), fileName)
        tableRef?.push()?.setValue(appUser)

        val bitmapDrawable = binding.imageView.drawable
        val bitmap = bitmapDrawable.toBitmap()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val arr = stream.toByteArray()

        /*lifecycleScope.launch {
            val db = Room.databaseBuilder(applicationContext, LocalDB::class.java, "avatars").build()
            val photoObj = Avatar(Random.nextInt(100), arr)
            db.getPhoto().insert(photoObj)
            finish()
        }*/
        lifecycleScope.launch {
            val db = Room.databaseBuilder(applicationContext, Database::class.java, "tunes").build()
            val photoObj = Avatar(Random.nextInt(100), arr, Random.nextInt(100).toString())
            db.playList().insert(photoObj)
            finish()
        }

    }

    companion object{
        private val IMAGE_PICK_CODE = 1
        private val PERMISSION_CODE = 2
    }
    private fun pickImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            binding.imageView.setImageURI(data?.data)
            if(data?.data != null){
                imageUri = data.data!!
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImage()
                }
            }
        }
    }
}

