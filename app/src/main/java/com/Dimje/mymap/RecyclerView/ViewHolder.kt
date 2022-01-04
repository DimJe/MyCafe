package com.Dimje.mymap.RecyclerView

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.Dimje.mymap.Cafeinfo
import com.Dimje.mymap.Document
import com.Dimje.mymap.MainActivity
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.R
import kotlinx.android.synthetic.main.layout_recycler_item.view.*

class ViewHolder(itemView: View,
                 recyclerviewInterface: RecyclerViewInterface):
                 RecyclerView.ViewHolder(itemView),
                 View.OnClickListener
{

    private val CafeName = itemView.cafe_name
    private val CafeAddress = itemView.cafe_address
    private val CafePoint = itemView.cafe_point

    private var myRecyclerviewInterface : RecyclerViewInterface? = null

    // 기본 생성자
    init {
        Log.d(TAG, "MyViewHolder - init() called")

        itemView.setOnClickListener(this)
        this.myRecyclerviewInterface = recyclerviewInterface

    }


    // 데이터와 뷰를 묶는다.
    fun bind(myModel: Document){
        Log.d(TAG, "MyViewHolder - bind() called")

        // 텍스트뷰 와 실제 텍스트 데이터를 묶는다.
        CafeName.text = myModel.place_name
        CafeAddress.text = myModel.address_name

    }

    override fun onClick(p0: View?) {
        Log.d(TAG, "MyViewHolder - onClick() called")

        this.myRecyclerviewInterface?.onItemClicked(adapterPosition)

    }


}
