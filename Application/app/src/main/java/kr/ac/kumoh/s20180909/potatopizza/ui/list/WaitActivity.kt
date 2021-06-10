package kr.ac.kumoh.s20180909.potatopizza.ui.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.ac.kumoh.s20180909.potatopizza.R

class WaitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait)
        val bar: ProgressBar = findViewById(R.id.progressBar)

        GlobalScope.launch {
            try{
                var i:Int = 0
                while(i<200){
                    bar.progress = i*1
                    Thread.sleep(100)
                    i+=1
                }
                bar.progress = i*1
                Thread.sleep(500)
            }catch(e: InterruptedException){
                e.printStackTrace()
            }
            //delay(20000L) //20초
            val intent = Intent(applicationContext, FindResultActivity::class.java) //결과창
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) //인텐트 스택 모두 제거
            startActivity(intent)
            finish() //액티비티 종료
        }
    }
}