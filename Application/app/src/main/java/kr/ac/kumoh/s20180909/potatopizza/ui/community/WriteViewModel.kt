package kr.ac.kumoh.s20180909.potatopizza.ui.community

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.StringRequest
import kr.ac.kumoh.s20180909.potatopizza.MySingleton
import org.json.JSONObject

class WriteViewModel(application: Application) : AndroidViewModel(application) {
    var mResult: JSONObject? = null
    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL = "https://potato-pizza-mytz.run.goorm.io"
    }
    private var mQueue: RequestQueue
    val imageLoader: ImageLoader
    init {
        mQueue = MySingleton.getInstance(application).requestQueue
        imageLoader = MySingleton.getInstance(application).imageLoader
    }
    override fun onCleared(){
        super.onCleared()
        mQueue.cancelAll(QUEUE_TAG)
    }
    fun requestList(image: String, name: String, pw: String, content: String, title: String){
        val url: String = "$SERVER_URL/write"
        val postRequest = object : StringRequest(Method.POST, url, Response.Listener<String>
        { response ->

        }, Response.ErrorListener {})
        {
            // 서버에 대이터 전달
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params.put("name", name)
                params.put("pw", pw)
                params.put("content", content)
                params.put("title", title)
                params.put("img", image)
                return params
            }

        }
        mQueue.add(postRequest)
        //MySingleton.getInstance(getApplication()).addToRequestQueue(postRequest)
    }
}