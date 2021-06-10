package kr.ac.kumoh.s20180909.potatopizza.ui.list

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.delay
import kr.ac.kumoh.s20180909.potatopizza.R
import kr.ac.kumoh.s20180909.potatopizza.R.id
import kr.ac.kumoh.s20180909.potatopizza.R.layout
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlinx.coroutines.*


//사진 업로드 및 기타 데이터 데이터베이스로 전달 후, 검색 결과 출력
class FindActivity : AppCompatActivity() {
    var profileImageBase64:String = ""
    private lateinit var findViewModel: FindViewModel
    lateinit var selectSpinner: String
    lateinit var selectSpinner2: String
    lateinit var selectRadio: String
    var checkSpinner: Boolean = false
    private var checkImage: Boolean = false
    var checkRadio: Boolean = false
    var currentImageURL : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.fragment_find)
        findViewModel = ViewModelProviders.of(this).get(FindViewModel::class.java)
        val spinner: Spinner = findViewById(id.spinner)
        val spinner2: Spinner = findViewById(id.spinner2)
        val searchBtn: Button = findViewById(id.btn_search)
        val imageBtn: Button = findViewById(id.btn_UploadPicture)
        val radioGroup: RadioGroup = findViewById(id.radioGroup)

        imageBtn.setOnClickListener {
            openGallery()
        }
        searchBtn.setOnClickListener {
            val id: Int = radioGroup.checkedRadioButtonId

            if (currentImageURL != null)
                checkImage = true

            if (id == -1)
                checkRadio = false
            else {
                checkRadio = true
                val radioButton: RadioButton = findViewById(id)
                selectRadio = radioButton.text as String
            }
            check()
        }

        setAdapterSpinner(spinner, R.array.spinner_region)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                checkSpinner = false
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectSpinner = spinner.selectedItem.toString()
                checkSpinner = selectSpinner.isNotEmpty()
                when(position){
                    1 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_seoul)
                    }
                    2 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_busan)
                    }
                    3 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_daegu)
                    }
                    4 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_incheon)
                    }
                    5 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_gwangju)
                    }
                    6 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_daejeon)
                    }
                    7 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_ulsan)
                    }
                    8 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_sejong)
                    }
                    9 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_gyeonggi)
                    }
                    10 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_gangwon)
                    }
                    11 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_chung_buk)
                    }
                    12 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_chung_nam)
                    }
                    13 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_jeon_buk)
                    }
                    14 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_jeon_nam)
                    }
                    15 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_gyeong_buk)
                    }
                    16 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_gyeong_nam)
                    }
                    17 -> {
                        setAdapterSpinner(spinner2, R.array.spinner_region_jeju)
                    }
                }
            }
        }
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectSpinner2 = spinner2.selectedItem.toString()
            }
        }
    }

    //Toast.makeText(applicationContext, currentImageURL.toString(), Toast.LENGTH_SHORT).show()
    //사진, spinner, radioBtn 모든 항목을 체크하였는지 검사
    private fun check(){
        if (checkImage && checkRadio && checkSpinner) {
            //Toast.makeText(applicationContext, "전부 다 선택됨", Toast.LENGTH_SHORT).show()
            sendDataToServer()    //Server에 전송
        }
        else
            Toast.makeText(applicationContext, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
    }

    //사용자가 입력한 데이터 Server로 전송
    private fun sendDataToServer() {
        //Toast.makeText(this, profileImageBase64,  Toast.LENGTH_LONG).show()
        GlobalScope.launch {
            findViewModel.requestList(profileImageBase64, selectSpinner, selectSpinner2, selectRadio)
            delay(2500L) //1.7초
            startActivity(Intent(applicationContext, WaitActivity::class.java)) //대기 activity
            finish() //현재 액티비티 종료
        }
    }

    private fun setAdapterSpinner(spinner: Spinner, place: Int){
        ArrayAdapter.createFromResource(
            this,
            place,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        if (place != R.array.spinner_region)
            checkSpinner = true
    }
    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        startActivityForResult(intent, 102)
    }
    // 절대경로 변환
    fun absolutelyPath(path: Uri): String? {

        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = contentResolver.query(path, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = index?.let { c?.getString(it) }

        return result
    }

    // 카메라로 촬영한 사진의 썸네일을 가져와 이미지뷰에 띄워줌
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            //Toast.makeText(this, "사진이 업로드되었습니다!", Toast.LENGTH_LONG).show()
        }

        if (requestCode == 102 && resultCode == Activity.RESULT_OK){
            currentImageURL = intent?.data
            var imagePath: String? = currentImageURL?.let { absolutelyPath(it) } //이미지 절대 경로
            // TODO 여기서부터 Base64 인코딩부분
            val ins: InputStream? = currentImageURL?.let {
                applicationContext.contentResolver.openInputStream(
                    it
                )
            }
            val img: Bitmap = BitmapFactory.decodeStream(ins)
            ins?.close()
            val resized = Bitmap.createScaledBitmap(img, 256, 256, true);
            val byteArrayOutputStream = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            val outStream = ByteArrayOutputStream()
            val res: Resources = resources
            profileImageBase64 = Base64.encodeToString(byteArray, NO_WRAP)
            // TODO 여기까지

            // 이미지 뷰에 선택한 이미지 출력
            val imageview: ImageView = findViewById(id.pet_image)
            imageview.setImageURI(currentImageURL)
            try {
                //이미지 선택 후 처리
            }catch (e: Exception){
                e.printStackTrace()
            }
        } else{
            //Log.d("ActivityResult", "something wrong")
        }

    }
}
