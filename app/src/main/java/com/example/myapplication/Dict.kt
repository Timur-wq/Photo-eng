package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentDictBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    lateinit var recViewAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>

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
        // Inflate the layout for this fragment


        getData()
        return binding.root
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
    var lablesLi = mutableListOf<MutableList<String>>()
    var wordLi = mutableListOf<String>()
    var imageList = mutableListOf<Bitmap>()
    var itemList = mutableListOf<ListItem>()
    var itemLi = mutableListOf<ExtraItem>()

    private fun getData() {
        val dialog = activity?.let { Dialog(it) }
        dialog?.setContentView(R.layout.progress_dialog)
        dialog?.setCancelable(false)
        dialog?.show()

        auth = FirebaseAuth.getInstance()
        table = FirebaseDatabase.getInstance().getReference(auth?.uid!!).child("Dict")
        val vListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(dialog?.isShowing!!){
                    dialog.dismiss()
                }
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
        table.addValueEventListener(vListener)
    }

    fun translate(wordLi1: MutableList<MutableList<String>>){
        val sharedPrefs = activity?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val langId = sharedPrefs?.getInt("Lang", 0)
        val res = activity?.applicationContext?.resources
        val langArr = res?.getStringArray(R.array.countries)
        val lang = langId?.let { langArr?.get(it) }//получаем язык, на который надо перевести


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

        wordLi = wordLi.distinct() as MutableList<String>
        var str = ""

        for(i in wordLi){
            j++
            translator.translate(i)
                .addOnSuccessListener {
                    val item = ExtraItem(i, it)
                    str += "$i/" + "$it;"
                    //dbList.add(item1)
                    itemLi.add(item)
                    if(j == wordLi.size){
                        binding.recView.layoutManager = LinearLayoutManager(activity?.applicationContext)
                        val adapter = ExtraRecyclerAdapter(itemLi as ArrayList<ExtraItem>, object : onItemClickListener{
                            override fun OnItemClick(position: Int) {
                                val i = Intent(activity?.applicationContext, WebViewActivity::class.java)
                                i.putExtra("Word", wordLi.get(position))
                                startActivity(i)
                            }

                        })
                        binding.recView.adapter = adapter

                        val sharedPrefs = activity?.getSharedPreferences("dict", Context.MODE_PRIVATE)
                        val s = sharedPrefs?.getString("DictStr", "")
                        val editor = sharedPrefs?.edit()
                        editor.apply {
                            this!!.putString("DictStr", str)
                        }?.apply()



                    }

                }.addOnFailureListener{

                }
        }


        /*private fun getData(){
            val dialog = activity?.let { Dialog(it) }
            dialog?.setContentView(R.layout.progress_dialog)
            dialog?.setCancelable(false)
            dialog?.show()

            auth = FirebaseAuth.getInstance()
            table = FirebaseDatabase.getInstance().getReference(auth?.uid!!).child("Dict")
            val vListener = object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(i in snapshot.children){
                        var model = i.getValue(Dictonary::class.java)
                        model?.wordList?.let { lablesLi.add(it) }
                        model?.imgUri?.let { urisLi.add(it) }
                    }

                    for(i in urisLi){
                        val storage = FirebaseStorage.getInstance().reference.child("detected/${i}jpg")
                        val storgae1 = storage
                        storage.getBytes(1024*1024).addOnSuccessListener {
                            if(dialog?.isShowing!!){
                                dialog.dismiss()
                            }
                            val bitmap = BitmapFactory.decodeByteArray(it, 0, (it.size).toInt())
                            val k: Float = getMetrics1() * getDensity()/bitmap.width
                            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.width * k).toInt(), (bitmap.height * k).toInt(), false)
                            val item = ListItem(scaledBitmap)
                            itemList.add(item)

                        }.addOnFailureListener{
                            Toast.makeText(activity?.applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }*/


        }

        //table.addValueEventListener(vListener)

    //}

    /*private fun getMetrics1(): Float {
        val display = activity?.windowManager?.defaultDisplay
        val outMetrics: DisplayMetrics = DisplayMetrics()
        display?.getMetrics(outMetrics)
        val density: Float = resources.displayMetrics.density
        val w = outMetrics.widthPixels/density
        return w.toFloat() - 32
    }

    private fun getDensity(): Float{
        val display = activity?.windowManager?.defaultDisplay
        val outMetrics = DisplayMetrics()
        display?.getMetrics(outMetrics)
        return resources.displayMetrics.density
    }*/
}