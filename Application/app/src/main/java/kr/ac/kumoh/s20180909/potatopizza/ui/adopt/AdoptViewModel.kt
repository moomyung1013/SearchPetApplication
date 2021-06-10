package kr.ac.kumoh.s20180909.potatopizza.ui.adopt

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.StringRequest
import kr.ac.kumoh.s20180909.potatopizza.MySingleton
import org.json.JSONObject

class AdoptViewModel(application: Application) : AndroidViewModel(application) {
    var mResult: JSONObject? = null
    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL = "https://potato-pizza-mytz.run.goorm.io"
    }
    private var mQueue: RequestQueue = MySingleton.getInstance(application).requestQueue
    val imageLoader: ImageLoader = MySingleton.getInstance(application).imageLoader
    override fun onCleared(){
        super.onCleared()
        mQueue.cancelAll(QUEUE_TAG)
    }
    fun requestList(kind: String, place: String, variety: String, gender: String){
        val url: String = "$SERVER_URL/adopt"
        val postRequest = object : StringRequest(Method.POST, url, Response.Listener<String>
        { response ->

        }, Response.ErrorListener {})
        {
            // 서버에 대이터 전달
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params.put("kind", kind)
                params.put("place", place)
                params.put("variety", variety)
                params.put("gender", gender)
                return params
            }

        }
        mQueue.add(postRequest)

        //MySingleton.getInstance(getApplication()).addToRequestQueue(postRequest)
    }
}