package com.example.myapplication

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.myapplication.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences("dict", Context.MODE_PRIVATE)
        val str = sharedPrefs?.getString("DictStr", "")
        //Toast.makeText(activity?.applicationContext, "$str", Toast.LENGTH_SHORT).show()
        var wordList: MutableList<String> = str?.split(";") as MutableList<String>
        binding.ok.visibility = View.INVISIBLE
        //тестирование по 10 словам
        val newWordList = mutableListOf<String>()
        wordList.removeAt(wordList.size-1)
        if(wordList?.size!! > 10){
            newWordList.add(wordList.get(0))
            wordList.removeAt(0)
            newWordList.add(wordList.get(0))
            wordList.removeAt(0)
            newWordList.add(wordList.get(0))
            wordList.removeAt(0)
            newWordList.add(wordList.get(0))
            wordList.removeAt(0)
            newWordList.add(wordList.get(0))
            wordList.removeAt(0)
            newWordList.add(wordList.get(wordList.size-1))
            wordList.removeAt(wordList.size-1)
            newWordList.add(wordList.get(wordList.size-1))
            wordList.removeAt(wordList.size-1)
            newWordList.add(wordList.get(wordList.size-1))
            wordList.removeAt(wordList.size-1)
            newWordList.add(wordList.get(wordList.size-1))
            wordList.removeAt(wordList.size-1)
            newWordList.add(wordList.get(wordList.size-1))
            wordList.removeAt(wordList.size-1)
        }else{
            newWordList.addAll(wordList)
        }
        var index = 0
        var words = newWordList.get(index).split("/")
        binding.textView.text = words.get(0)

        binding.button5.setOnClickListener {
            binding.ok.visibility = View.VISIBLE
            binding.button5.visibility = View.INVISIBLE
            if(binding.translation.text.toString().equals(words.get(1))){
                binding.ln.setBackgroundColor(Color.argb(100, 100, 255, 100))
            }else{
                binding.ln.setBackgroundColor(Color.argb(100, 255, 100, 100))
                binding.howCorrect.text = words.get(1)
            }
        }

        binding.ok.setOnClickListener{
            binding.howCorrect.text = ""
            binding.ok.visibility = View.INVISIBLE
            binding.button5.visibility =View.VISIBLE
            if(!binding.translation.text.toString().equals(words.get(1))) {
                wordList.add(words.get(0) + "/" + words.get(1))
            }
            index++
            if(index == 10){
                var str1 = ""
                for(i in wordList){
                    str1 += "$i;"
                }
                val editor = sharedPrefs.edit()
                Toast.makeText(applicationContext, "$str1", Toast.LENGTH_SHORT).show()
                editor.apply {
                    putString("DictStr", str1)
                }.apply()
                //val intent = Intent(activity?.applicationContext, MainActivity::class.java)
                //startActivity(intent)
                //activity?.finish()
            }
            else if(index < 10){
                words = newWordList.get(index).split("/")
                binding.textView.text = words.get(0)
                binding.translation.setText("")
                binding.ln.setBackgroundColor(Color.rgb(241, 238, 238))
            }
        }

    }
}