package kr.ac.kumoh.s20180909.potatopizza.ui.community

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.item_list.*
import kr.ac.kumoh.s20180909.potatopizza.R
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class WriteActivity : AppCompatActivity() {

    var profileImageBase64:String = ""
    var name: String = ""
    var pw: String = ""
    var content: String = ""
    var title: String = ""
    val TAG = "CHECK"
    var currentImageURL : Uri? = null
    lateinit var imageView: ImageView
    private lateinit var writeViewModel: WriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        writeViewModel = ViewModelProviders.of(this).get(WriteViewModel::class.java)

        val editTextId: EditText = findViewById(R.id.editTextId)
        val editTextPw: EditText = findViewById(R.id.editTextPw)
        val editTextContent: EditText = findViewById(R.id.editTextContent)
        val editTextTitle: EditText = findViewById(R.id.editTextTitle)
        val btn_selectPicture: Button = findViewById(R.id.btn_selectPicture)
        val btn_save: Button = findViewById(R.id.btn_save)
        imageView = findViewById(R.id.imageView)
        btn_selectPicture.setOnClickListener {
            openGallery()
        }
        imageView.setOnClickListener {
            openGallery()
        }

        btn_save.setOnClickListener {
            name = editTextId.text.toString()
            pw = editTextPw.text.toString()
            content = editTextContent.text.toString()
            title = editTextTitle.text.toString()

            if((name=="" || pw=="" || content=="" || title=="" || currentImageURL == null)){
                //사진도 넣어서 넣었는지 확인하려면 뒤에 붙이기
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_LONG).show()
            }
            else{
                writeViewModel.requestList(profileImageBase64, name, pw, content, title)
                val intent = Intent()
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        startActivityForResult(intent, 102)
    }

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
            profileImageBase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
            // TODO 여기까지

            // 이미지 뷰에 선택한 이미지 출력
            imageView.setImageURI(currentImageURL)
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