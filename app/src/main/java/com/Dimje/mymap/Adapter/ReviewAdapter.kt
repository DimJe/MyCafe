package com.Dimje.mymap.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Dimje.mymap.Review
import com.Dimje.mymap.databinding.LayoutReviewRecyclerItemBinding

class ReviewAdapter(val reviewList: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = LayoutReviewRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

    override fun getItemCount() : Int {
        return reviewList.size
    }
    inner class ReviewViewHolder(private val binding : LayoutReviewRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(review: Review){
            binding.reviewDate.text = review.date
            binding.reviewText.text = review.review
        }

    }
}