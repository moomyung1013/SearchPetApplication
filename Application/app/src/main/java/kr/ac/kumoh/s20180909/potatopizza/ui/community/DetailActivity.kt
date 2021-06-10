package kr.ac.kumoh.s20180909.potatopizza.ui.community

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20180909.potatopizza.MySingleton
import kr.ac.kumoh.s20180909.potatopizza.R

class DetailActivity : AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title: TextView = findViewById(R.id.title)
        val date: TextView = findViewById(R.id.date)
        val nickname: TextView = findViewById(R.id.nickname)
        val content: TextView = findViewById(R.id.content)
        val image: NetworkImageView = findViewById(R.id.image)

        image.background = resources.getDrawable(R.drawable.img_setting, null)
        image.clipToOutline = true

        title.text = intent.getStringExtra("title")
        date.text = intent.getStringExtra("date") + " 작성됨"
        nickname.text = intent.getStringExtra("name")
        content.text = intent.getStringExtra("content")
        image.setImageUrl(intent.getStringExtra("image"), MySingleton.getInstance(application).imageLoader)
    }
}