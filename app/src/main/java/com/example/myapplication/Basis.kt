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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentBasisBinding
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Basis.newInstance] factory method to
 * create an instance of this fragment.
 */
class Basis : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentBasisBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBasisBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefs = activity?.getSharedPreferences("profs", Context.MODE_PRIVATE)
        var profSphereId = sharedPrefs?.getInt("Prof", 0)
        when(profSphereId){
            0 ->{
                loadData(profSphereId.toString())

                dialog = activity?.let { Dialog(it) }!!//инициализируем диалог, который будет отображаться во время загрузки
                initLoadingDialog(dialog)
            }
            1->{
                loadData(profSphereId.toString())

                dialog = activity?.let { Dialog(it) }!!//инициализируем диалог, который будет отображаться во время загрузки
                initLoadingDialog(dialog)
            }
            2->{
                Toast.makeText(activity?.applicationContext, "Данный раздел ещё не добавлен", Toast.LENGTH_LONG).show()
            }
            3->{
                Toast.makeText(activity?.applicationContext, "Данный раздел ещё не добавлен", Toast.LENGTH_LONG).show()
            }
            4->{
                Toast.makeText(activity?.applicationContext, "Данный раздел ещё не добавлен", Toast.LENGTH_LONG).show()
            }
            5->{
                Toast.makeText(activity?.applicationContext, "Данный раздел ещё не добавлен", Toast.LENGTH_LONG).show()
            }
        }

    }

    lateinit var table: DatabaseReference

    lateinit var EnList: List<String>
    lateinit var RuList: List<String>
    var listOfExtra: MutableList<ExtraItem> = mutableListOf<ExtraItem>()

    private fun loadData(id: String){
        var strEN = ""
        table = FirebaseDatabase.getInstance().getReference(id)
        table.get().addOnSuccessListener {
            strEN = it.value as String
            EnList = strEN.split("; ")
            loadRuData(id)
        }
    }

    private fun loadRuData(id: String){
        var strRu = ""
        var id1 = id + 't'
        var str = ""
        table = FirebaseDatabase.getInstance().getReference(id1)
        table.get().addOnSuccessListener {
            strRu = it.value as String
            RuList = strRu.split("; ")

            var i = 0
            while (i < EnList.size){
                str += "${EnList.get(i)}/" + "${RuList.get(i)};"
                val item = ExtraItem(EnList[i], RuList[i])
                listOfExtra.add(item)
                i++
            }
            if(dialog.isShowing){
                dialog.dismiss()
            }
            initRecyclerView()
            putWordsIntoSharedPrefs(str)
        }
    }

    private fun initLoadingDialog(dialog: Dialog){
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun initRecyclerView(){
        binding.recView.layoutManager = LinearLayoutManager(activity?.applicationContext)

        val lac = LayoutAnimationController(AnimationUtils.loadAnimation(activity, R.anim.slide_in))
        lac.delay = 0.20f
        lac.order = LayoutAnimationController.ORDER_NORMAL
        binding.recView.layoutAnimation = lac
        val adapter = ExtraRecyclerAdapter(listOfExtra as ArrayList<ExtraItem>, object : onItemClickListener{
            override fun OnItemClick(position: Int) {
                val i = Intent(activity?.applicationContext, WebViewActivity::class.java)
                i.putExtra("Word", EnList.get(position))
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Basis.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Basis().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}