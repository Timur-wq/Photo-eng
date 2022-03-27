package com.example.myapplication

import android.app.Activity
import android.app.Dialog
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
import androidx.core.app.ActivityCompat.startActivityForResult
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
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
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

    lateinit var dialog: Dialog

    lateinit var downloadUri: String

    lateinit var binding: ActivityRegistrationBinding
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.clipToOutline = true//чтобы картинка профиля пользователя была скруглённой

        //по нажатию на кнопку осуществляется выбор аватарки пользователя из галереи (предварительно у пользователя запрашивается доступ к галереи)
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

        //отслеживаем ввод пароля
        binding.pass1.doOnTextChanged { text, start, before, count ->
            if(text?.length!! <= 7){
                binding.passL.error = resources.getString(R.string.pass_rule)
            }else{
                binding.passL.error = null
            }
        }

        binding.button.setOnClickListener {//осуществляем регистрацию пользователя, проверяя поля ввода на наличие символов

            val user = auth.currentUser
            binding.button.visibility = View.VISIBLE
            val email: String = binding.email.text.toString()
            val password: String = binding.pass1.text.toString()
            val name1: String = binding.name1.text.toString()
            val name2: String = binding.name2.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty() && name1.isNotEmpty() && name2.isNotEmpty() && imageUri != null){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        downloadLangModel()
                    }
                }
            }else{
                val toast = Toast.makeText(applicationContext, resources.getString(R.string.fill_fields), Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }


        }
    }

    lateinit var options: TranslatorOptions

    //устанавливаем на устройство языковую модель языка, установленного на устройсте
    private fun downloadLangModel(){
        val lang = Locale.getDefault().language

        dialog = Dialog(this)
        dialog.setContentView(R.layout.download_dial)
        dialog.setCancelable(false)
        dialog.show()


        if(lang.equals("ru")){
            options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.RUSSIAN)
                .build()

        }else if(lang.equals("be")){
            options= TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.BELARUSIAN)
                .build()
        }
        else if(lang.equals("uk")){
            options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.UKRAINIAN)
                .build()
        }


        val translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                uploadImage()
            }.addOnFailureListener{

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
        val uri = ref.downloadUrl.toString()//получаем ссылку, по которой можно найти изображение в Firebase storage

        loadUserDataToDatabase()

        ref.putFile(imageUri).addOnSuccessListener {//загружаем картинку в Firebase storage
            val i = Intent(this, Authorisation::class.java)
            startActivity(i)
            if(dialog.isShowing){
                dialog.dismiss()
            }
            finish()
        }

    }

    private fun loadUserDataToDatabase(){//загружаем данные пользователя в поля Realtime database
        val tableRef = auth.currentUser?.let { FirebaseDatabase.getInstance().getReference(it.uid) }
        val appUser = AppUser(binding.name1.text.toString(), binding.name2.text.toString(), binding.email.text.toString(), binding.pass1.text.toString(), imageUri.toString())
        tableRef?.push()?.setValue(appUser)
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
                val bitmap = binding.imageView.drawable.toBitmap()
                imageUri = data?.data!!
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


