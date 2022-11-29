package com.Dimje.mymap.RecyclerView

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.Dimje.mymap.Cafeinfo
import com.Dimje.mymap.Document
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.databinding.LayoutRecyclerItemBinding

class ViewHolder(binding: LayoutRecyclerItemBinding,
                 recyclerviewInterface: RecyclerViewInterface):
                 RecyclerView.ViewHolder(binding.root),
                 View.OnClickListener
{

    private val cafeName = binding.cafeName
    private val cafeAddress = binding.cafeAddress
    private val cafePoint = binding.cafePoint
    private val cafeDistance = binding.cafeDistance

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
        cafeName.text = myModel.place_name
        cafeAddress.text = myModel.address_name
        cafePoint.text = "0.0/5"
        cafeDistance.text = (myModel.distance.toDouble()/1000).toString()+"km"

    }

    override fun onClick(p0: View?) {
        Log.d(TAG, "MyViewHolder - onClick() called")

        this.myRecyclerviewInterface?.onItemClicked(adapterPosition)

    }


}
