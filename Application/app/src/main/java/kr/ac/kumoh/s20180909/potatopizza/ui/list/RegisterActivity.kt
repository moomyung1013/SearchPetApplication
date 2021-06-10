package kr.ac.kumoh.s20180909.potatopizza.ui.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_find.*
import kr.ac.kumoh.s20180909.potatopizza.MainActivity
import kr.ac.kumoh.s20180909.potatopizza.R
import kr.ac.kumoh.s20180909.potatopizza.R.layout
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    var profileImageBase64:String = ""
    var mCurrentPhotoPath: String? = null
    var locationManager: LocationManager? = null
    var date: String? = null
    var gender: String = ""
    var kind: String = ""
    var safe: String = ""
    var variety: String = ""
    var phone: String = ""
    var place: String = ""
    var place2: String = ""
    val TAG = "CHECK"
    var currentImageURL : Uri? = null

    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.fragment_register)

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)

        val btn_takePicture: Button = findViewById(R.id.btn_takePicture)
        val btn_register: Button = findViewById(R.id.btn_register)
        val pet_image: ImageView = findViewById(R.id.pet_image)
        val editText: EditText = findViewById(R.id.editText)
        val spinner: Spinner = findViewById(R.id.spinner)
        val placeSpinner: Spinner = findViewById(R.id.spinnerRegisterPlace)
        val placeSpinner2: Spinner = findViewById(R.id.spinnerRegisterPlace2)
        val btn_selectImage: Button = findViewById(R.id.btn_selectPicture)
        val radioGroupKind: RadioGroup = findViewById(R.id.registerKind)
        val radioGroupGender: RadioGroup = findViewById(R.id.registerGender)
        val radioGroupSafe: RadioGroup = findViewById(R.id.registerSafe)
        var checkRadio: Boolean = false
        var checkSpinner: Boolean = false


        btn_selectImage.setOnClickListener {
            openGallery()
        }
        radioGroupKind.setOnCheckedChangeListener { radioGroup, i ->
            val radio: RadioButton = findViewById(i)
            val selected: String = radio.text as String
            if(selected == "강아지")
                setAdapterSpinner(spinner, R.array.spinner_variety_dog)
            else
                setAdapterSpinner(spinner, R.array.spinner_variety_cat)
        }
        radioGroupSafe.setOnCheckedChangeListener { radioGroup, i ->
            val radio: RadioButton = findViewById(i)
            val selected: String = radio.text as String
            if(selected == "예")
                editText.visibility = View.VISIBLE
            else{
                editText.setText("")
                editText.visibility = View.INVISIBLE
            }
        }

        btn_takePicture.setOnClickListener {
            takePicture()
            //getArea()
        }

        btn_register.setOnClickListener {
            val id: Int = radioGroupKind.checkedRadioButtonId
            val id2: Int = radioGroupGender.checkedRadioButtonId
            val id3: Int = radioGroupSafe.checkedRadioButtonId
            if (id == -1 || id2 == -1 || id3 == -1)
                checkRadio = false
            else {
                checkRadio = true
                val kindRadioButton: RadioButton = findViewById(id)
                val genderRadioButton: RadioButton = findViewById(id2)
                val safeRadioButton: RadioButton = findViewById(id3)
                kind = kindRadioButton.text as String
                gender = genderRadioButton.text as String
                safe = safeRadioButton.text as String
            }
            phone = editText.text.toString()
            //val temp: String = "$kind $variety $gender $safe $phone $place $place2"
            checkNotBool(checkRadio, checkSpinner)

            val intent = Intent()
            setResult(RESULT_OK, intent)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                variety = spinner.selectedItem.toString()
            }
        }

        setAdapterSpinner(placeSpinner, R.array.spinner_region)
        placeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                checkSpinner = false
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0)
                    checkSpinner = false
                else{
                    checkSpinner = true
                    place = placeSpinner.selectedItem.toString()
                }
                when(position){
                    1 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_seoul)
                    }
                    2 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_busan)
                    }
                    3 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_daegu)
                    }
                    4 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_incheon)
                    }
                    5 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_gwangju)
                    }
                    6 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_daejeon)
                    }
                    7 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_ulsan)
                    }
                    8 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_sejong)
                    }
                    9 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_gyeonggi)
                    }
                    10 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_gangwon)
                    }
                    11 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_chung_buk)
                    }
                    12 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_chung_nam)
                    }
                    13 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_jeon_buk)
                    }
                    14 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_jeon_nam)
                    }
                    15 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_gyeong_buk)
                    }
                    16 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_gyeong_nam)
                    }
                    17 -> {
                        setAdapterSpinner(placeSpinner2, R.array.spinner_region_jeju)
                    }
                }
            }
        }
        placeSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                place2 = placeSpinner2.selectedItem.toString()
            }
        }

    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        startActivityForResult(intent, 102)
    }

    private fun checkNotBool(checkRadio: Boolean, checkSpinner: Boolean){
        if (checkRadio && checkSpinner) {
            registerViewModel.requestList(profileImageBase64, place, place2, kind, variety, gender, safe, phone)
            Toast.makeText(this, "동물의 정보가 저장되었습니다!", Toast.LENGTH_SHORT).show()
            //val intent: Intent = Intent(applicationContext, MainActivity::class.java) //결과창
            //startActivity(Intent(applicationContext, MainActivity::class.java))
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
        else
            Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
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
    }
    fun getArea(){
        //지역 가지고 오는 기능
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        var userLocation: Location = getLatLng()

        var latitude : Double? = null
        var longitude : Double? = null

        if(userLocation != null){
            latitude = userLocation.latitude
            longitude = userLocation.longitude
            Log.d("CheckCurrentLocation", "현재 내 위치 값 : $latitude, $longitude")

            var mgeoCoder = Geocoder(this, Locale.KOREAN)
            var mResultList : List<Address>? = null
            try {
                mResultList = mgeoCoder.getFromLocation(
                    latitude, longitude, 1
                )
            } catch (e: IOException){
                e.printStackTrace()
            }
            if(mResultList != null){
                Log.d("CheckCurrentLocation", mResultList[0].getAddressLine(0))
                place = mResultList[0].getAddressLine(0)
            }
        }
    }

    private fun getLatLng() : Location {
        var currentLatLng: Location? = null
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 102)
            getLatLng()
        } else{
            val locationProvider = LocationManager.GPS_PROVIDER
            currentLatLng = locationManager?.getLastKnownLocation(locationProvider)
        }
        return currentLatLng!!
    }

    fun takePicture() {
        //카메라 인텐트 실행하는 합수
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //MediaStore.ACTION_IMAGE_CAPTURE = 카메라 앱 띄우는 액션 정보
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createFile()
            } catch (e: IOException) {
                //e.printStackTrace();
            }
            if (photoFile != null) {
                val uri = FileProvider.getUriForFile(this, "kr.ac.kumoh.s20180909.potatopizza.fileprovider", photoFile)
                //Toast.makeText(this, "사진 uri : " + uri, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "사진 uri" + uri)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                startActivityForResult(intent, 101)
            }
        }
    }

    private fun createFile(): File {
        //촬영한 사진 이미지 파일로 저장하는 함수
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        date = SimpleDateFormat("yyyyMMdd").format(Date())
        //파일 이름
        val filename = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(filename, ".jpg", storageDir)
        mCurrentPhotoPath = image.absolutePath
        Log.d(TAG, "사진명 filename : " + filename)
        return image
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "사진이 업로드되었습니다!", Toast.LENGTH_LONG).show()
            //Log.d(TAG, "사진 경로 mCurrentPhotoPath" + mCurrentPhotoPath)

            val file = File(mCurrentPhotoPath)
            //val bitmap: Bitmap?
            if (Build.VERSION.SDK_INT >= 29) {
                val source = ImageDecoder.createSource(contentResolver, Uri.fromFile(file))
                try {
                    bitmap = ImageDecoder.decodeBitmap(source)
                    if (bitmap != null) {
                        pet_image!!.setImageBitmap(bitmap)
                    }
                    val resized = Bitmap.createScaledBitmap(bitmap!!, 256, 256, true);
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    resized.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)
                    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                    val outStream = ByteArrayOutputStream()
                    val res: Resources = resources
                    profileImageBase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                    if (bitmap != null) {
                        pet_image!!.setImageBitmap(bitmap)
                    }
                    val resized = Bitmap.createScaledBitmap(bitmap!!, 256, 256, true);
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    resized.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)
                    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                    val outStream = ByteArrayOutputStream()
                    val res: Resources = resources
                    profileImageBase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        else if (requestCode == 102 && resultCode == Activity.RESULT_OK){
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
            profileImageBase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
            // TODO 여기까지

            // 이미지 뷰에 선택한 이미지 출력
            val imageview: ImageView = findViewById(R.id.pet_image)
            imageview.setImageURI(currentImageURL)
            try {
                //이미지 선택 후 처리
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}