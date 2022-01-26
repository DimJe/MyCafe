package com.Dimje.mymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import com.Dimje.mymap.API.CallAPI.Companion.result
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.MainActivity.Companion.mDatabase
import kotlinx.android.synthetic.main.activity_add_review.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddReview : AppCompatActivity() {
    var tastePoint : Int = 0
    var beautyPoint : Int = 0
    var studyPoint : Int = 0
    var totalReview : String = ""
    var position : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_add_review)

        position = intent.getIntExtra("position",-1)

        taste.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.taste_1 -> tastePoint = 1
                R.id.taste_2 -> tastePoint = 2
                R.id.taste_3 -> tastePoint = 3
                R.id.taste_4 -> tastePoint = 4
                R.id.taste_5 -> tastePoint = 5
            }
            Log.d(TAG, "tastePoint : $tastePoint ")
        }
        beauty.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.beauty_1 -> beautyPoint = 1
                R.id.beauty_2 -> beautyPoint = 2
                R.id.beauty_3 -> beautyPoint = 3
                R.id.beauty_4 -> beautyPoint = 4
                R.id.beauty_5 -> beautyPoint = 5
            }
            Log.d(TAG, "beautyPoint : $beautyPoint ")
        }
        study.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.study_1 -> studyPoint = 1
                R.id.study_2 -> studyPoint = 2
                R.id.study_3 -> studyPoint = 3
                R.id.study_4 -> studyPoint = 4
                R.id.study_5 -> studyPoint = 5
            }
            Log.d(TAG, "studyPoint : $studyPoint ")
        }
        sumitReview.setOnClickListener {
            totalReview = editReview.text.toString()
            val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val review : Review = Review(totalReview,tastePoint.toString(),beautyPoint.toString(),studyPoint.toString(),date)
            mDatabase.child(result.value!!.documents[position].place_name).push().setValue(review)
            finish()


        }



    }
}