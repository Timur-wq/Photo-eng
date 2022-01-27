package com.example.myapplication

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var options: Options

    private val data = DataTest(this)

    private var liveData = MutableLiveData<String>()

    lateinit var table: DatabaseReference
    lateinit var auth: FirebaseAuth

    lateinit var dialog: Dialog

    val ctx = this


    val REQ_CODE1 = 1
    val REQ_CODE2 = 2;
    override fun onCreate(savedInstanceState: Bundle?) {//Bundle - для сохранеия состояния
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.background = null
        options = savedInstanceState?.getParcelable(OPTIONS) ?: Options.DEFAULT
        var i: Intent
        binding.bottomNavigation.selectedItemId = R.id.home
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.profile -> {i = Intent(this, Profile::class.java)
                    startActivity(i)
                    overridePendingTransition(0, 0)
                    finish()
                }
                R.id.settings ->{
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

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.vp2.adapter = adapter

        val lang = Locale.getDefault().language
        TabLayoutMediator(binding.tl, binding.vp2){tab, position ->
            when(position){
                0 ->{
                    if(lang.equals("ru")) {
                        tab.text = "Добавить"
                    }else if(lang.equals("be")){
                        tab.text = "Дадаць"
                    }else if(lang.equals("uk")){
                        tab.text = "Додати"
                    }
                }
                1 -> {
                    if(lang.equals("ru")) {
                        tab.text = "Словарь"
                    }else if(lang.equals("be")){
                        tab.text = "Слоўнік"
                    }else if(lang.equals("uk")){
                        tab.text = "Словник"
                    }
                }
                2->{
                    if(lang.equals("ru")) {
                        tab.text = "Тестирование"
                    }else if(lang.equals("be")){
                        tab.text = "Тэсціраванне"
                    }else if(lang.equals("uk")){
                        tab.text = "Тестування"
                    }
                }
            }
        }.attach()

        val JI = Teacher("JI", 45)
        val TG = Student("TG", 17)
        val user = object : Person("Marat", 14){
            override val permission
            get() = "Default permission"
        }

        //проверка на null-безопасность

        //val user1 = FirebaseAuth.getInstance().currentUser
        //user1?.delete()

        lifecycle.addObserver(data)

        lifecycleScope.launch(Dispatchers.IO) {
            liveData.postValue("Hello, liveData")
        }




    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState?.run {
            putParcelable(OPTIONS, options)
        }
    }

    private fun initDB(){
        table = FirebaseDatabase.getInstance().getReference()
        auth = FirebaseAuth.getInstance()


    }


    companion object{
        @JvmStatic val OPTIONS = "OPTIONS"
    }

    override fun onStart() {
        super.onStart()
    }


}