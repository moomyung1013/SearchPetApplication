package kr.ac.kumoh.s20180909.potatopizza.ui.list

import android.app.Activity
import android.app.AppComponentFactory
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kr.ac.kumoh.s20180909.potatopizza.R
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.util.*

class ListFragment : Fragment() {

    private lateinit var listViewModel: ListViewModel
    private val myAdapter = ListAdapter()
    val TAG = "CHECK"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listViewModel = ViewModelProvider(activity as AppCompatActivity).get(ListViewModel::class.java)
        listViewModel.list.observe(viewLifecycleOwner, Observer<ArrayList<ListViewModel.petList>>{
            myAdapter.notifyDataSetChanged()
        })
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        val IsResult = root.findViewById<RecyclerView>(R.id.IsResult)
        IsResult.apply{
            layoutManager = GridLayoutManager(activity, 2)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = myAdapter
        }
        listViewModel.requestList()
        val registerButton: Button = root.findViewById(R.id.registerButton)

        registerButton.setOnClickListener{
            startActivityForResult(Intent(context, RegisterActivity::class.java), 102)
        }

        val findButton: Button = root.findViewById(R.id.findButton)

        findButton.setOnClickListener {
           // findNavController().navigate(R.id.find)
            startActivity(Intent(context, FindActivity::class.java))
        }

        val swipe: SwipeRefreshLayout = root.findViewById(R.id.swipe)
        swipe.setOnRefreshListener {
            val ft : FragmentTransaction = parentFragmentManager.beginTransaction()
            ft.detach(this).attach(this).commit()
            swipe.isRefreshing = false
        }

        settingPermission()
        return root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 102 && resultCode == Activity.RESULT_OK){
            //Toast.makeText(activity, "나는 끝", Toast.LENGTH_SHORT).show()
            listViewModel.requestList()
            myAdapter.notifyDataSetChanged()
            refreshFragment(this, parentFragmentManager)
        }
    }

    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager){
        val ft : FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }

    inner class ListAdapter: RecyclerView.Adapter<ListAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val txDay = itemView.findViewById<TextView>(R.id.findDay)
            val txData= itemView.findViewById<TextView>(R.id.petPlace)
            val txData2 = itemView.findViewById<TextView>(R.id.phoneNum)
            val txData3 = itemView.findViewById<TextView>(R.id.petKind)
            val txData4 = itemView.findViewById<TextView>(R.id.petVariety)
            val txData5 = itemView.findViewById<TextView>(R.id.state)
            val txData6 = itemView.findViewById<TextView>(R.id.petGender)
            val petImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image)

            init{
                petImage.setDefaultImageResId(R.drawable.dangdang)
            }
        }

        override fun getItemCount(): Int {
            return listViewModel.getSize()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ListAdapter.ViewHolder {
            val view = layoutInflater.inflate(R.layout.item_test, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
            var variety = listViewModel.getList(position).variety
            var gender = listViewModel.getList(position).gender
            var phone = listViewModel.getList(position).phone
            var state = "O"
            if (variety=="null")
                variety = "미상"
            if (gender=="null")
                gender = "미상"
            if (phone=="null"){
                phone = "-"
                state = "X"
            }
            holder.txDay.text = "${listViewModel.getList(position).date} 발견"
            holder.txData.text = "${listViewModel.getList(position).place} - ${listViewModel.getList(position).place2}"
            holder.txData2.text = " $phone"
            holder.txData3.text = "${listViewModel.getList(position).kind}"
            holder.txData4.text = "$variety"
            holder.txData5.text = "$state"
            holder.txData6.text = "$gender"
            holder.petImage.setImageUrl(listViewModel.getImageUrl(position), listViewModel.imageLoader)
        }
    }
    fun settingPermission(){
        var permis = object : PermissionListener {
            //어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                //Toast.makeText(getActivity(), "권한 허가", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                //Toast.makeText(getActivity(), "권한 거부", Toast.LENGTH_SHORT).show()
                //ActivityCompat.finishAffinity(this) // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(getActivity())
            .setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.CAMERA)
            .check()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permisson: " + permissions[0] + " was " + grantResults[0])
        }
    }
}