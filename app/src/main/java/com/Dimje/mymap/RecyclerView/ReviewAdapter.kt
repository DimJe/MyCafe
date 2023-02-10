package com.Dimje.mymap.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Dimje.mymap.Review
import com.Dimje.mymap.databinding.LayoutReviewRecyclerItemBinding

class ReviewAdapter : RecyclerView.Adapter<ReviewViewHolder>(){
    private var reviewList = ArrayList<Review>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = LayoutReviewRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReviewViewHolder(binding)
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
class ReviewViewHolder(binding : LayoutReviewRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){

    val date = binding.reviewDate
    val text = binding.reviewText

    fun bind(review: Review){
        date.text = review.date
        text.text = review.review
    }

}