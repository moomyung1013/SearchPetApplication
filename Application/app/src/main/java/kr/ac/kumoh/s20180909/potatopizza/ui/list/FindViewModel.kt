package kr.ac.kumoh.s20180909.potatopizza.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.volley.*
import com.android.volley.toolbox.*
import kr.ac.kumoh.s20180909.potatopizza.MySingleton
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.URLEncoder

class FindViewModel(application: Application) : AndroidViewModel(application) {
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
    fun requestList(image: String, place1: String, place2: String, kind: String){
        val url: String = "$SERVER_URL/search"
        val postRequest = object : StringRequest(Method.POST, url, Response.Listener<String>
        { response ->

        }, Response.ErrorListener {})
        {
            // 서버에 대이터 전달
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params.put("place", place1)
                params.put("place2", place2)
                params.put("kind", kind)
                params.put("name", image)
                return params
            }

        }

        mQueue.add(postRequest)

        //MySingleton.getInstance(getApplication()).addToRequestQueue(postRequest)
    }
}