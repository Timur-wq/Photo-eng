package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.UserModels.RecyclerViewModel
import com.example.myapplication.databinding.FragmentDictBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Dict.newInstance] factory method to
 * create an instance of this fragment.
 */
class Dict : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = "null"
    private var param2: String? = "null"


    lateinit var table: DatabaseReference
    lateinit var auth: FirebaseAuth

    lateinit var dialog: Dialog

    private val recyclerViewModel: RecyclerViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentDictBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDictBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = activity?.let { Dialog(it) }!!//инициализируем диалог, который будет отображаться во время загрузки
        initLoadingDialog(dialog)

        //как только viewModel загрузит словарь, слова в который мы добавляли сами, то запустится функция по их переводу
        recyclerViewModel.translatorData.observe(activity as LifecycleOwner, {
            translate(it)
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Dict.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String): Dict {
            val args = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
            val fragment = Dict()
            fragment.arguments = args
            return fragment
        }
    }
    var lablesLi = mutableListOf<MutableList<String>>()//структура данных Изменяемый список
    var wordLi = mutableListOf<String>()
    var itemLi = mutableListOf<ExtraItem>()

    private fun initLoadingDialog(dialog: Dialog){
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCancelable(false)
        dialog.show()
    }

    //узнаём, какой язык перевода установлен в настройках
    private fun getLanguageFromSettings(): String{
        val sharedPrefs = activity?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val langId = sharedPrefs?.getInt("Lang", 0)
        val res = activity?.applicationContext?.resources
        val langArr = res?.getStringArray(R.array.countries)
        val lang = langId?.let { langArr?.get(it) }
        return lang.toString()
    }

    //осуществляем перевод загруженных слов
    private fun translate(wordLi1: MutableList<MutableList<String>>){
        if(dialog.isShowing){
            dialog.dismiss()
        }
        //получаем язык, на который надо перевести
        val lang = getLanguageFromSettings()

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
        for(i in wordLi1){
            for(j in i){
                wordLi.add(j)
            }
        }

        val translator = Translation.getClient(options)
        var j = 0

        if(wordLi.size > 0) {
            wordLi = wordLi.distinct() as MutableList<String>//комаеда distinct создаёт множестов, то есть исключает повторяющиеся слова, если они есть
        }
        var str = ""

        for(i in wordLi){
            j++
            translator.translate(i)
                .addOnSuccessListener {
                    val item = ExtraItem(i, it)
                    str += "$i/" + "$it;"
                    itemLi.add(item)
                    if(j == wordLi.size){
                        initRecyclerView()//инициализируем анимированный список слов
                        putWordsIntoSharedPrefs(str)//записываем строку в shared preferences
                    }

                }.addOnFailureListener{

                }
        }
    }

    //инициализируем список слов из загруженного словаря (список всплывает анимированно)
    private fun initRecyclerView(){
        binding.recView.layoutManager = LinearLayoutManager(activity?.applicationContext)

        val lac = LayoutAnimationController(AnimationUtils.loadAnimation(activity, R.anim.slide_in))
        lac.delay = 0.20f
        lac.order = LayoutAnimationController.ORDER_NORMAL
        binding.recView.layoutAnimation = lac
        val adapter = ExtraRecyclerAdapter(itemLi as ArrayList<ExtraItem>, object : onItemClickListener{
            override fun OnItemClick(position: Int) {
                val i = Intent(activity?.applicationContext, WebViewActivity::class.java)
                i.putExtra("Word", wordLi.get(position))
                startActivity(i)
            }

        })
        binding.recView.adapter = adapter
    }

    private fun putWordsIntoSharedPrefs(str: String){
        val sharedPrefs = activity?.getSharedPreferences("dict", Context.MODE_PRIVATE)
        val s = sharedPrefs?.getString("DictStr", "")
        val editor = sharedPrefs?.edit()
        editor.apply {
            this!!.putString("DictStr", str)
        }?.apply()
    }
}