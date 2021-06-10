package kr.ac.kumoh.s20180909.potatopizza.ui.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20180909.potatopizza.MainActivity
import kr.ac.kumoh.s20180909.potatopizza.R
import java.util.ArrayList

class FindResultActivity : AppCompatActivity() {
    private lateinit var findResultModel: FindResultViewModel
    private val myAdapter = FindAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_result)

        findResultModel = ViewModelProviders.of(this).get(FindResultViewModel::class.java)

        findResultModel.list.observe(this, Observer<ArrayList<FindResultViewModel.resultList>>
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
        findResultModel.requestList()
    }
    inner class FindAdapter: RecyclerView.Adapter<FindAdapter.ViewHolder>(){
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
            return findResultModel.getSize()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindAdapter.ViewHolder {
            val view = layoutInflater.inflate(R.layout.item_list2, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: FindAdapter.ViewHolder, position: Int) {
            var variety = findResultModel.getList(position).variety
            var gender = findResultModel.getList(position).gender
            var phone = findResultModel.getList(position).phone
            var state = "O"
            if (variety=="null")
                variety = "미상"
            if (gender=="null")
                gender = "미상"
            if (phone=="null"){
                phone = "-"
                state = "X"
            }
            holder.txDay.text = "발견 날짜 : ${findResultModel.getList(position).date}"
            holder.txData.text = "장소: ${findResultModel.getList(position).place} - ${findResultModel.getList(position).place2}"
            holder.txData2.text = "임보상태: $state      보호자정보: $phone"
            holder.txData3.text = "종류: ${findResultModel.getList(position).kind}      품종: $variety      성별: $gender"
            holder.petImage.setImageUrl(findResultModel.getImageUrl(position), findResultModel.imageLoader)
        }

    }
}