package kr.ac.kumoh.s20180909.potatopizza.ui.community

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import com.google.android.material.snackbar.Snackbar
import kr.ac.kumoh.s20180909.potatopizza.R
import java.util.ArrayList

class CommunityFragment : Fragment() {

    private lateinit var communityViewModel: CommunityViewModel

    private val myAdapter = CommunityAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        communityViewModel =
                ViewModelProviders.of(this).get(CommunityViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_community, container, false)
        val textView: TextView = root.findViewById(R.id.listTitle)

        communityViewModel.list.observe(viewLifecycleOwner, Observer<ArrayList<CommunityViewModel.writeList>>{
            myAdapter.notifyDataSetChanged()
        })

        val IsResult = root.findViewById<RecyclerView>(R.id.IsResult)
        IsResult.apply{
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = myAdapter
        }
        communityViewModel.requestList()

        val fab: View = root.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "게시글을 작성합니다", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()

            //startActivity(Intent(context, WriteActivity::class.java))

            startActivityForResult(Intent(context, WriteActivity::class.java), 102)
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 102 && resultCode == Activity.RESULT_OK){
            //Toast.makeText(getActivity(), "나는 끝", Toast.LENGTH_SHORT).show()
            communityViewModel.requestList()
            myAdapter.notifyDataSetChanged()
            refreshFragment(this, parentFragmentManager)
        }
    }

    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager){
        var ft : FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }

    inner class CommunityAdapter: RecyclerView.Adapter<CommunityAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val postNickname = itemView.findViewById<TextView>(R.id.postNickname)
            val postDate = itemView.findViewById<TextView>(R.id.postDate)
            val postTitle= itemView.findViewById<TextView>(R.id.postTitle)
            val postContent = itemView.findViewById<TextView>(R.id.postContent)
            val writeImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image)

            init{
                writeImage.setDefaultImageResId(R.drawable.dangdang)
            }
        }

        override fun getItemCount(): Int {
            return communityViewModel.getSize()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityAdapter.ViewHolder {
            val view = layoutInflater.inflate(R.layout.item_community, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: CommunityAdapter.ViewHolder, position: Int) {
            var content = communityViewModel.getList(position).content

            //var selectedItem = SparseBooleanArray()
            //var prePosition: Int = 0

            if(content.length >= 30)
                content = content.substring(0, 30) + "..."
            holder.postNickname.text = "${communityViewModel.getList(position).name}"
            holder.postDate.text = "${communityViewModel.getList(position).date} 에 작성됨"
            holder.postTitle.text = "${communityViewModel.getList(position).title}"
            holder.postContent.text = "$content"
            holder.writeImage.setImageUrl(communityViewModel.getImageUrl(position), communityViewModel.imageLoader)

            holder.itemView.setOnClickListener {

                //holder.postContent.text = "${communityViewModel.getList(position).content}"

                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("name", communityViewModel.getList(position).name)
                intent.putExtra("date", communityViewModel.getList(position).date)
                intent.putExtra("title", communityViewModel.getList(position).title)
                intent.putExtra("content", communityViewModel.getList(position).content)
                intent.putExtra("image", communityViewModel.getImageUrl(position))
                startActivityForResult(intent, 103)

                //if(selectedItem.get(position)) {
                //    selectedItem.delete(position)

                    //if (content.length >= 30)
                    //    content = content.substring(0, 30) + "..."
                //} else{
                //    selectedItem.delete(prePosition)

                //    selectedItem.put(position, true)

                //    holder.postContent.text = "${communityViewModel.getList(position).content}"
                //}

                //notifyItemChanged(position)

                //prePosition = position
            }
        }
    }
}