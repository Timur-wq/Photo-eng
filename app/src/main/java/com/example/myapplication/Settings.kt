package com.example.myapplication

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.work.*
import com.example.myapplication.databinding.ActivitySettingsBinding
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

//экран - страница настроек приложения
class Settings : AppCompatActivity() {

    var language: Int = 0

    lateinit var dialog: Dialog


    lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.background = null
        binding.bottomNavigation.selectedItemId = R.id.settings
        var i: Intent
        loadSettings()

        //навигация по нижней панели
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    i = Intent(this, MainActivity::class.java)
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

        //отслеживаем выбранный из выпадающего списка язык
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

            //ocуществляем загрузку языковой модели того языка, который был выбран из выпадающего списка
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
        binding.apply {
            langSpinner.setSelection(language)
        }
    }

    companion object{
        val KEY = "Key"
    }

}

