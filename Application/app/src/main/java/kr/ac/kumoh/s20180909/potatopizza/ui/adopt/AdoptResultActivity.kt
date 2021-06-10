package kr.ac.kumoh.s20180909.potatopizza.ui.adopt

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20180909.potatopizza.R
import kr.ac.kumoh.s20180909.potatopizza.ui.list.FindResultViewModel
import java.util.ArrayList

class AdoptResultActivity: AppCompatActivity(){
    private lateinit var adoptResultModel: AdoptResultViewModel
    private val myAdapter = AdoptAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adopt_result)

        adoptResultModel = ViewModelProviders.of(this).get(AdoptResultViewModel::class.java)

        adoptResultModel.list.observe(this, Observer<ArrayList<AdoptResultViewModel.resultList>>
        {
            myAdapter.notifyDataSetChanged()
        })

        val IsResult = findViewById<RecyclerView>(R.id.IsResult)
        IsResult.apply{
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = myAdapter
        }
        adoptResultModel.requestList()
    }
    inner class AdoptAdapter: RecyclerView.Adapter<AdoptAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val txDay = itemView.findViewById<TextView>(R.id.findDay)
            val txData= itemView.findViewById<TextView>(R.id.petPlace)
            val txData2 = itemView.findViewById<TextView>(R.id.phoneNum)
            val txData3 = itemView.findViewById<TextView>(R.id.petData)
            val petImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image)

            init{
                petImage.setDefaultImageResId(R.drawable.dangdang)
            }
        }

        override fun getItemCount(): Int {
            return adoptResultModel.getSize()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdoptAdapter.ViewHolder {
            val view = layoutInflater.inflate(R.layout.item_list3, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: AdoptAdapter.ViewHolder, position: Int) {
            var variety = adoptResultModel.getList(position).variety
            var gender = adoptResultModel.getList(position).gender
            var phone = adoptResultModel.getList(position).phone
            var state = "O"
            if (variety=="null")
                variety = "미상"
            if (gender=="null")
                gender = "미상"
            if (phone=="null"){
                phone = "-"
                state = "X"
            }
            holder.txDay.text = "발견 날짜 : ${adoptResultModel.getList(position).date}"
            holder.txData.text = "장소: ${adoptResultModel.getList(position).place} - ${adoptResultModel.getList(position).place2}"
            holder.txData2.text = "임보상태: $state      보호자정보: $phone"
            holder.txData3.text = "종류: ${adoptResultModel.getList(position).kind}      품종: $variety      성별: $gender"
            holder.petImage.setImageUrl(adoptResultModel.getImageUrl(position), adoptResultModel.imageLoader)
        }


    }
}