package com.Dimje.mymap.RecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Dimje.mymap.Cafeinfo
import com.Dimje.mymap.Document
import com.Dimje.mymap.MainActivity
import com.Dimje.mymap.R
import com.Dimje.mymap.databinding.LayoutRecyclerItemBinding

class RecyclerViewAdapter(myRecyclerviewInterface: RecyclerViewInterface): RecyclerView.Adapter<ViewHolder>() {
    private var modelList = ArrayList<Document>()

    private var myRecyclerviewInterface :RecyclerViewInterface? = null

    // 생성자
    init {
        this.myRecyclerviewInterface = myRecyclerviewInterface
    }

    // 뷰홀더가 생성 되었을때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // 연결할 레이아웃 설정
        val binding = LayoutRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding,myRecyclerviewInterface!!)
    }

    // 목록의 아이템수
    override fun getItemCount(): Int {
        return this.modelList.size
    }

    // 뷰와 뷰홀더가 묶였을때
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(MainActivity.TAG, "MyRecyclerAdapter - onBindViewHolder() called / position: $position")
        holder.bind(this.modelList[position])

    }

    // 외부에서 데이터 넘기기
    fun submitList(modelList: ArrayList<Document>){
        this.modelList = modelList
    }

}
