package com.example.myapplication

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Contacts
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.myapplication.alarm.AlarmReceiver
import com.example.myapplication.databinding.ActivitySettingsBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.*
import java.util.concurrent.TimeUnit

class Settings : AppCompatActivity() {

    val CHANNEL_ID = "CHANNEL_ID"
    var language: Int = 0
    var allowNoti: Boolean = false

    lateinit var dialog: Dialog

    lateinit var manager: AlarmManager


    lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        createNotificationChannel()
        setContentView(binding.root)
        binding.bottomNavigation.background = null
        binding.bottomNavigation.selectedItemId = R.id.settings
        var i: Intent
        loadSettings()
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    overridePendingTransition(0, 0)
                    finish()
                }
                R.id.profile -> {
                    i = Intent(this, Profile::class.java)
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
        binding.tick.setColorFilter(Color.argb(255, 255, 255, 255))

        //createNotificationChannel()
        binding.langSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?){

            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, itemId: Long){
                //Toast.makeText(applicationContext, "${adapterView?.getItemAtPosition(position).toString()}", Toast.LENGTH_SHORT).show()
                language = position
                saveSettings()
            }
        }

        binding.tick.setOnClickListener{
            dialog = Dialog(this)
            dialog.setContentView(R.layout.download_dial)
            dialog.setCancelable(false)
            dialog.show()

            val lang = binding.langSpinner.getItemAtPosition(language).toString()
            var options: TranslatorOptions = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.RUSSIAN)
                .build()
            when(lang){
                "Русский"->{
                    options = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.RUSSIAN)
                        .build()
                }
                "Руская мова"->{
                    options = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.RUSSIAN)
                        .build()
                }
                "Російська мова"->{
                    options = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.RUSSIAN)
                        .build()
                }
                "Беларуская мова"->{
                    options = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.BELARUSIAN)
                        .build()
                }
                "Білоруська мова"->{
                    options = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.BELARUSIAN)
                        .build()
                }
                "Белорусский"->{
                    options = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.BELARUSIAN)
                        .build()
                }
                "Українська мова"->{
                    options = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.UKRAINIAN)
                        .build()
                }
                "Украинский"->{
                    options = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.UKRAINIAN)
                        .build()
                }
                "Украінская мова"->{
                    options = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.UKRAINIAN)
                        .build()
                }
            }


            val translator = Translation.getClient(options)

            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    if(dialog.isShowing){
                        dialog.dismiss()
                    }
                    saveSettings()
                }.addOnFailureListener{

                }


            //saveSettings()
            //Toast.makeText(applicationContext, "Настройки сохранены", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()
    }

    private fun saveSettings(){

        val data = Data.Builder()
            .putInt(KEY, 125)
            .build()

        /*if(binding.switch1.isChecked){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(!android.provider.Settings.canDrawOverlays(this)){
                    val intent = Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package: " + packageName))
                    startActivity(intent)
                }
            }
            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = 23
            calendar[Calendar.MINUTE] = 15
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0

            manager = getSystemService(ALARM_SERVICE) as AlarmManager

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, getActionPendingIntent())*/

            //val alarmClockInfo: AlarmManager.AlarmClockInfo = AlarmManager.AlarmClockInfo(calendar.timeInMillis, getAlarmInfoIntent())

            //manager.setAlarmClock(alarmClockInfo, getActionPendingIntent())
        val sharedPrefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.apply{
            putInt("Lang", language)
            putBoolean("SettingsVisited", true)
        }.apply()
    }


    private fun loadSettings(){
        val sharedPrefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        language = sharedPrefs.getInt("Lang", 0)
        allowNoti = sharedPrefs.getBoolean("AllowNoti", false)
        binding.apply {
            langSpinner.setSelection(language)
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("photo-english International", "name1", NotificationManager.IMPORTANCE_HIGH)
            val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    private fun getAlarmInfoIntent(): PendingIntent{
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getActionPendingIntent(): PendingIntent{
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent
    }

    companion object{
        val KEY = "Key"
    }

}

