package kr.ac.kumoh.s20180909.potatopizza.ui.list

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import kr.ac.kumoh.s20180909.potatopizza.MySingleton
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class ListViewModel(application: Application) : AndroidViewModel(application) {
    data class petList(var id: Int, var date: String, var variety: String, var gender: String,
                       var place: String, var place2: String, var kind: String, var phone: String,
                       var img: String)

    companion object{
        const val QUEUE_TAG = "VolleyRequest"
        val SERVER_URL = "https://potato-pizza-mytz.run.goorm.io"
    }
    val list = MutableLiveData<ArrayList<petList>>()
    private val pet = ArrayList<petList>()
    private var mQueue: RequestQueue
    val imageLoader: ImageLoader
    init {
        list.value = pet
        mQueue = MySingleton.getInstance(application).requestQueue
        imageLoader = MySingleton.getInstance(application).imageLoader
    }

    fun getImageUrl(i: Int): String {
        return "$SERVER_URL/image/" + URLEncoder.encode(pet[i].img, "utf-8")
    }
    fun getList(i :Int) = pet[i]
    fun getSize() = pet.size

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
                pet.clear()
                parseJson(it)
                list.value = pet
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
            val id = item.getInt("id")
            val date = item.getString("date")
            val variety = item.getString("variety")
            val img = item.getString("img")
            val gender = item.getString("gender")
            val place = item.getString("place")
            val place2 = item.getString("place2")
            val kind = item.getString("kind")
            val phone = item.getString("phone")

            pet.add(petList(id, date, variety, gender, place, place2, kind, phone, img))

        }
    }
}