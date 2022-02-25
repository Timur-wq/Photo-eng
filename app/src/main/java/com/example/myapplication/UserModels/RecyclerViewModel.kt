package com.example.myapplication.UserModels

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.R
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class RecyclerViewModel(app: Application): AndroidViewModel(app) {
    var translatorData: MutableLiveData<MutableList<ExtraItem>>
    lateinit var table: DatabaseReference
    lateinit var auth: FirebaseAuth
    var lablesLi = mutableListOf<MutableList<String>>()
    var wordLi = mutableListOf<String>()
    var extraItemList = mutableListOf<ExtraItem>()
    var activity = app

    init {
        translatorData = MutableLiveData()
        loadData()
    }

    fun loadData(){
        auth = FirebaseAuth.getInstance()
        table = FirebaseDatabase.getInstance().getReference(auth?.uid!!).child("Dict")
        val vListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    var model = i.getValue(Dictonary::class.java)
                    model?.wordList?.let { lablesLi.add(it) }
                }
                translate(lablesLi)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
    }

    fun translate(wordLi1: MutableList<MutableList<String>>) {
        val sharedPrefs = activity?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val langId = sharedPrefs?.getInt("Lang", 0)
        val res = activity?.applicationContext?.resources
        val langArr = listOf<String>("Русский", "Украинский", "Белорусский")
        val lang = langId?.let { langArr?.get(it) }//получаем язык, на который надо перевести


        var options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.RUSSIAN)
            .build()

        when (lang) {
            "Русский" -> {
                options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.RUSSIAN)
                    .build()
            }
            "Руская мова" -> {
                options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.RUSSIAN)
                    .build()
            }
            "Російська мова" -> {
                options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.RUSSIAN)
                    .build()
            }
            "Беларуская мова" -> {
                options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.BELARUSIAN)
                    .build()
            }
            "Білоруська мова" -> {
                options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.BELARUSIAN)
                    .build()
            }
            "Белорусский" -> {
                options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.BELARUSIAN)
                    .build()
            }
            "Українська мова" -> {
                options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.UKRAINIAN)
                    .build()
            }
            "Украинский" -> {
                options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.UKRAINIAN)
                    .build()
            }
            "Украінская мова" -> {
                options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.UKRAINIAN)
                    .build()
            }
        }
        for (i in wordLi1) {
            for (j in i) {
                wordLi.add(j)
            }
        }


        val translator = Translation.getClient(options)
        var j = 0

        wordLi = wordLi.distinct() as MutableList<String>

        var str = ""

        for(i in wordLi){
            j++
            translator.translate(i)
                .addOnSuccessListener {
                    val item = ExtraItem(i, it)
                    extraItemList.add(item)
                    if (j == wordLi.size){
                        translatorData.postValue(extraItemList)
                    }
                    //dbList.add(item1)

                }.addOnFailureListener{

                }
        }
    }
}