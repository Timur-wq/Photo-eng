package com.example.myapplication

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myapplication.AlertDialog.AlertDialog
import com.example.myapplication.databinding.ActivityTestBinding

//тестирование по 10 словам
class TestActivity : AppCompatActivity(), DialogInterface.OnClickListener {
    lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences("dict", Context.MODE_PRIVATE)
        val str = sharedPrefs?.getString("DictStr", "")//получаем строку, состоящую из слов, которые будут предложены на тестирование.

        if(str?.length!! > 0){
            var wordList: MutableList<String> = str?.split(";") as MutableList<String>
            binding.ok.visibility = View.INVISIBLE

            //выбираем 10 слов для тестирования
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
            }else{
                newWordList.addAll(wordList)
            }
            var index = 0
            var words = newWordList.get(index).split("/")
            binding.textView.text = words.get(0)

            binding.button5.setOnClickListener {
                binding.ok.visibility = View.VISIBLE
                binding.button5.visibility = View.INVISIBLE
                val s = words.get(1).toLowerCase()
                if((binding.translation.text.toString().toLowerCase()).equals(s)){
                    binding.ln.setBackgroundColor(Color.argb(100, 100, 255, 100))
                }else{
                    binding.ln.setBackgroundColor(Color.argb(100, 255, 100, 100))
                    binding.howCorrect.text = words.get(1)
                }
            }
            var incorrect = 0

            //по нажатию на кнопку "ОК" проверяем корректность написания перевода слова
            //также считаем количество правильных и неправильных ответов
            binding.ok.setOnClickListener{
                binding.howCorrect.text = ""
                binding.ok.visibility = View.INVISIBLE
                binding.button5.visibility =View.VISIBLE
                val s = words.get(1).toLowerCase()
                if(!binding.translation.text.toString().toLowerCase().equals(s)) {
                    wordList.add(words.get(0) + "/" + words.get(1))
                    incorrect++
                }
                index++
                if(index == 10 || index == newWordList.size){
                    var str1 = ""
                    for(i in wordList){
                        str1 += "$i;"
                    }
                    val editor = sharedPrefs.edit()
                    editor.apply {
                        putString("DictStr", str1)
                    }.apply()

                    //переходим на следующий экран, где отобразим количктсво правильных и неправильных ответов
                    val intent = Intent(applicationContext, ResultActivity::class.java)
                    intent.putExtra("size", newWordList.size)
                    intent.putExtra("mistakes", incorrect)
                    startActivity(intent)
                    finish()
                }
                else if(index < 10){
                    words = newWordList.get(index).split("/")
                    binding.textView.text = words.get(0)
                    binding.translation.setText("")
                    binding.ln.setBackgroundColor(Color.rgb(241, 238, 238))
                }
            }
        }
        else{
            //отображаем пользователю сообщение, если словарь пустой
            AlertDialog().show(supportFragmentManager, "Empty Dial")
        }

    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        when(p1){
            DialogInterface.BUTTON_POSITIVE ->{
                finish()
            }
        }
    }
}