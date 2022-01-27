package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityExtraInfoBinding
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.async
import java.util.*
import kotlin.collections.ArrayList

class ExtraInfo : AppCompatActivity() {
    lateinit var binding: ActivityExtraInfoBinding
    lateinit var wordLi: ArrayList<String>
    lateinit var tempArrayList: ArrayList<ExtraItem>
    lateinit var newArrayList: ArrayList<String>

    var itemLi = arrayListOf<ExtraItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtraInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var options: TranslatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.RUSSIAN)
            .build()

        wordLi = intent.getStringArrayListExtra("words")!!
        translate()

        binding.rView.layoutManager = LinearLayoutManager(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar, menu)
        val item = menu?.findItem(R.id.app_bar_search)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("NOT YET IMPLEMENTED")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()
                val searchText = newText?.toLowerCase(Locale.getDefault())
                if(searchText?.isNotEmpty()!!){
                    itemLi.forEach {
                        if(it.engWord.toLowerCase(Locale.getDefault()).contains(searchText)){
                            tempArrayList.add(it)
                        }
                    }
                    binding.rView.adapter?.notifyDataSetChanged()
                }else{
                    tempArrayList.clear()
                    tempArrayList.addAll(itemLi)
                    binding.rView.adapter?.notifyDataSetChanged()
                }


                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    fun translate(){
        val sharedPrefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val langId = sharedPrefs.getInt("Lang", 0)
        val res = applicationContext.resources
        val langArr = res.getStringArray(R.array.countries)
        val lang = langArr[langId]//получаем язык, на который надо перевести
        

        var options = TranslatorOptions.Builder()
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
        var j = 0
        for(i in wordLi){
            j++
            translator.translate(i)
                .addOnSuccessListener {
                    val item = ExtraItem(i, it)
                    itemLi.add(item)
                    if(j == wordLi.size){
                        Collections.sort(itemLi)
                        tempArrayList = itemLi
                        val adapter = ExtraRecyclerAdapter(tempArrayList, object : onItemClickListener{
                            override fun OnItemClick(position: Int) {
                                val i = Intent(applicationContext, WebViewActivity::class.java)
                                i.putExtra("Word", wordLi.get(position))
                                startActivity(i)
                            }

                        })
                        binding.rView.adapter = adapter
                    }

                }.addOnFailureListener{

                }
        }

    }
}