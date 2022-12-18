package com.Dimje.mymap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.MainActivity.Companion.dbModel
import com.Dimje.mymap.MainActivity.Companion.model
import com.Dimje.mymap.RecyclerView.ReviewAdapter
import com.Dimje.mymap.databinding.ActivityReviewBinding
import kotlin.math.roundToInt

class ReviewActivity : AppCompatActivity() {
    var position : Int = 0
    var placeName = ""
    private val binding : ActivityReviewBinding by lazy {
        ActivityReviewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG, "ReviewActivity - onCreate : called ")

        position = intent.getIntExtra("position",-1)


        binding.cafeName.text = model.result.value!!.documents[position].place_name
        binding.cafeAddress.text = model.result.value!!.documents[position].address_name


        val reAdapter = ReviewAdapter()



        dbModel.getData(model.result.value!!.documents[position].place_name)

        dbModel.reviewList.observe(this) {
            reAdapter.submitList(it)
            binding.reviewRecyclerView.adapter = reAdapter
            binding.reviewRecyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.cafePoint.text = if (dbModel.count == 0) "맛있나요? : 0  이쁜가요? : 0  공부하기 좋은가요? : 0"
            else "맛있나요? : ${((dbModel.taste / dbModel.count) * 10).roundToInt() / 10f}  " +
                    "이쁜가요? : ${((dbModel.beauty / dbModel.count) * 10).roundToInt() / 10f}  " +
                    "공부하기 좋은가요? : ${((dbModel.study / dbModel.count) * 10).roundToInt() / 10f}"
        }


        binding.writeReview.setOnClickListener {
            val intent = Intent(this,AddReview::class.java)
            intent.putExtra("position",position)
            startActivity(intent)
        }
        binding.close.setOnClickListener {
            dbModel.reviewList.value!!.clear()
            finish()
        }
    }
}