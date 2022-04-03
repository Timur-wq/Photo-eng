package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.widget.Toast
import com.example.myapplication.databinding.ActivityAuthorisationBinding
import com.google.firebase.auth.FirebaseAuth


//экран авторизации пользователя
class Authorisation : AppCompatActivity() {
    lateinit var binding: ActivityAuthorisationBinding

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorisationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser != null){//если пользователь авторизован, то открываем главную страницу приложения
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }else{
            binding.buttonAuth.setOnClickListener { auth() }
            binding.button.setOnClickListener {//открываем страницу регистрации
                val i: Intent = Intent(this, Registration::class.java)
                startActivity(i)
                finish()
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState?.run {
            putString("EMAIL", binding.email.text.toString());
            putString("Password", binding.pass1.text.toString())
        }
    }

    private fun auth(){//осуществляем аутентификацию пользователя
        val email = binding.email.text.toString()
        val pass = binding.pass1.text.toString()
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this){task->
            if(task.isSuccessful){
                val i: Intent = Intent(this, Registration::class.java)
                startActivity(i)
                finish()
            }else{
                val toast = Toast.makeText(applicationContext, "Неверный email или пароль", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }
    }

}