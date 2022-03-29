import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.*
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.FragmentImageLoaderBinding
import com.example.myapplication.photoViewModel.PhotoFactory
import com.example.myapplication.photoViewModel.PhotoModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageLoader.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageLoader : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = "null"
    private var param2: String? = "null"
    lateinit var anim: Animation
    lateinit var anim1: Animation
    lateinit var mainAnim: Animation
    lateinit var mainAnim1: Animation

    var wordList = mutableListOf<String>()

    lateinit var table: DatabaseReference
    lateinit var auth: FirebaseAuth

    lateinit var binding: FragmentImageLoaderBinding
    lateinit var bitmapDet: Bitmap
    var byteArray = byteArrayOf()

    lateinit var photoModel: PhotoModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImageLoaderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ViewModel, которая будет хранить фотографию, выбираемую из галереи, в байтовом массиве
        photoModel = activity?.let { ViewModelProvider(it, PhotoFactory(requireActivity().application, byteArray)).get(PhotoModel::class.java) }!!
        photoModel.liveData.observe(requireActivity(), Observer {
            //val bytes = res?.get(0)?.photo
            val iStr: ByteArrayInputStream = ByteArrayInputStream(it)
            val o: BitmapFactory.Options = BitmapFactory.Options()
            val bitmap = BitmapFactory.decodeStream(iStr, null, o)
            //binding.avatar.setImageBitmap(bitmap)
            if(bitmap != null){
                binding.imageView4.setImageBitmap(bitmap)
                detect(bitmap)
            }
        })
        addFeaturesToViewElements()

        //по нажатию на кнопку осуществляется выбор изображения из галереи (предварительно у пользователя спрашивается разраешение на доступ к галерее)
        binding.gallery.setOnClickListener{
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if(activity?.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, REQ_CODE)
                }else{
                    pickImage()
                }
            }else{
                pickImage()
            }
        }

        binding.tick.setOnClickListener{
            //загружаем данные в Firebase
            auth = FirebaseAuth.getInstance()
            table = auth.currentUser?.let { it1 -> FirebaseDatabase.getInstance().getReference(it1.uid).child("Dict") }!!
            val dict = Dictonary(wordList)
            table.push().setValue(dict)
            val i = Intent(activity!!.applicationContext, MainActivity::class.java)
            startActivity(i)
            activity!!.finish()
        }
        binding.cansel.setOnClickListener{
            val i = Intent(activity!!.applicationContext, MainActivity::class.java)
            startActivity(i)
            activity!!.finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQ_CODE ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImage()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_CODE){
            if(data?.data != null){
                val inputStream: InputStream? = activity?.contentResolver?.openInputStream(data?.data!!)
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

                binding.imageView4.setImageBitmap(bitmap)
                bitmapDet = bitmap

                detect(bitmap)
                lifecycleScope.launch {
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                    byteArray = stream.toByteArray()
                    photoModel.liveData.value = byteArray
                }

            }
        }
    }
    private fun addFeaturesToViewElements(){
        anim = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.show_gallery)
        anim1 = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.hide_gallery)
        mainAnim = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.rotate_plus)
        mainAnim1 = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.rotate_plus1)

        binding.cansel.visibility = View.INVISIBLE
        binding.tick.visibility = View.INVISIBLE

        binding.cansel.setColorFilter(Color.argb(255, 255, 255, 255))
        binding.gallery.setColorFilter(Color.argb(255, 255, 255, 255))
        binding.tick.setColorFilter(Color.argb(255, 255, 255, 255))
    }

    private fun detect(bm: Bitmap?){
        binding.gallery.visibility = View.INVISIBLE

        val visionImg = FirebaseVisionImage.fromBitmap(bm!!)//bm - картинка типа Bitmap
        FirebaseVision.getInstance().onDeviceImageLabeler.processImage(visionImg)
            .addOnSuccessListener {
                val lang = getLanguageFromSettings()//получаем язык, установленный в настройках, на который надо переводить слова

                var options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.RUSSIAN)
                    .build()

                when(lang) {
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
                //Toast.makeText(activity?.applicationContext, "${it.get(0).text}", Toast.LENGTH_SHORT).show()

                for(i in it){
                    wordList.add(i.text)
                }

                val translator = Translation.getClient(options)
                var itemList: ArrayList<ExtraItem> = arrayListOf()
                var j = 0
                wordList = wordList.distinct() as MutableList<String>
                for(i in wordList){
                    j++
                    translator.translate(i)
                        .addOnSuccessListener {
                            val item = ExtraItem(i, it)
                            itemList.add(item)
                            if(j == wordList.size){
                                initRecyclerView(itemList)

                                binding.cansel.visibility = View.VISIBLE
                                binding.tick.visibility = View.VISIBLE
                            }

                        }.addOnFailureListener{

                        }
                }

            }
    }

    fun getParams() = requireArguments().getString(ARG_PARAM1) + " " + requireArguments().getString(ARG_PARAM2)

    private fun pickImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CODE)
    }


    companion object {

        @JvmStatic
        val REQ_CODE = 1
        val IMAGE_CODE = 2
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ImageLoader.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String): ImageLoader{
            val args = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
            val fragment = ImageLoader()
            fragment.arguments = args
            return fragment
        }
    }

    private fun getLanguageFromSettings(): String{
        val sharedPrefs = activity?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val langId = sharedPrefs?.getInt("Lang", 0)
        val res = activity?.applicationContext?.resources
        val langArr = res?.getStringArray(R.array.countries)
        val lang = langId?.let { langArr?.get(it) }
        return lang.toString()
    }

    private fun initRecyclerView(itemList: ArrayList<ExtraItem>){
        binding.recView.layoutManager = LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.VERTICAL, false)
        val adapter = ExtraRecyclerAdapter(itemList, object : onItemClickListener{
            override fun OnItemClick(position: Int) {
                val i = Intent(activity?.applicationContext, WebViewActivity::class.java)
                i.putExtra("Word", wordList.get(position))
                startActivity(i)
            }

        })
        binding.recView.adapter = adapter

        val lac = LayoutAnimationController(AnimationUtils.loadAnimation(activity, R.anim.slide_in))
        lac.delay = 0.20f
        lac.order = LayoutAnimationController.ORDER_NORMAL
        binding.recView.layoutAnimation = lac
    }


}