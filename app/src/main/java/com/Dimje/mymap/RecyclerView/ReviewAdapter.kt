package com.Dimje.mymap.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Dimje.mymap.R
import com.Dimje.mymap.Review
import kotlinx.android.synthetic.main.layout_review_recycler_item.view.*

class ReviewViewHolder(v: View) : RecyclerView.ViewHolder(v){

    val date = v.reviewDate
    val text = v.reviewText

    fun bind(review: Review){
        date.text = review.date
        text.text = review.review
    }

}

class ReviewAdapter : RecyclerView.Adapter<ReviewViewHolder>(){
    private var reviewList = ArrayList<Review>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val lm = LayoutInflater.from(parent.context).inflate(R.layout.layout_review_recycler_item,parent,false)
        return ReviewViewHolder(lm)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(this.reviewList[position])
    }

    override fun getItemCount() : Int {
        return reviewList.size
    }
    fun submitList(reviewList: ArrayList<Review>){
        this.reviewList = reviewList
    }


}