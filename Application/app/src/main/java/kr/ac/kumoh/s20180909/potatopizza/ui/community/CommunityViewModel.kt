package kr.ac.kumoh.s20180909.potatopizza.ui.community

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import kr.ac.kumoh.s20180909.potatopizza.MySingleton
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class CommunityViewModel (application: Application) : AndroidViewModel(application) {
    data class writeList(var name: String, var pw: String, var date: String, var content: String, var title: String, var img: String)

    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL = "https://potato-pizza-mytz.run.goorm.io/write_result"
    }
    val list = MutableLiveData<ArrayList<writeList>>()
    private val write = ArrayList<writeList>()
    private var mQueue: RequestQueue
    val imageLoader: ImageLoader
    init {
        list.value = write
        mQueue = MySingleton.getInstance(application).requestQueue
        imageLoader = MySingleton.getInstance(application).imageLoader
    }

    fun getImageUrl(i: Int): String {
        return "$SERVER_URL/image/" + URLEncoder.encode(write[i].img, "utf-8")
    }
    fun getList(i :Int) = write[i]
    fun getSize() = write.size

    override fun onCleared(){
        super.onCleared()
        mQueue.cancelAll(QUEUE_TAG)
    }
    fun requestList(){
        var request = JsonArrayRequest(
            Request.Method.GET,
            SERVER_URL,
            null,
            {
                write.clear()
                parseJson(it)
                list.value = write
            },
            {
                Toast.makeText(getApplication(),
                    it.toString(),
                    Toast.LENGTH_LONG).show()
            }
        )

        request.tag = QUEUE_TAG
        MySingleton.getInstance(getApplication()).addToRequestQueue(request)
    }
    private fun parseJson(items: JSONArray) {
        for (i in 0 until items.length()) {
            val item: JSONObject = items[i] as JSONObject
            val name = item.getString("name")
            val pw = item.getString("pw")
            val date = item.getString("date")
            val content = item.getString("content")
            val title = item.getString("title")
            val img = item.getString("img")

            write.add(writeList(name, pw, date, content, title, img))

        }
    }
}