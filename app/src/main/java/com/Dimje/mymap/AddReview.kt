package com.Dimje.mymap

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import com.Dimje.mymap.MainActivity.Companion.TAG
import com.Dimje.mymap.MainActivity.Companion.dbModel
import com.Dimje.mymap.databinding.ActivityAddReviewBinding

class AddReview : Activity() {
    var tastePoint : String = ""
    var beautyPoint : String = ""
    var studyPoint : String = ""
    var totalReview : String = ""
    var position : Int = 0
    //private lateinit var binding : ActivityAddReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_add_review)

        position = intent.getIntExtra("position",-1)
        val taste = findViewById<RadioGroup>(R.id.taste)
        val beauty = findViewById<RadioGroup>(R.id.beauty)
        val study = findViewById<RadioGroup>(R.id.study)
        val submitReview = findViewById<Button>(R.id.submitReview)
        val editReview = findViewById<EditText>(R.id.editReview)


        taste.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.taste_1 -> tastePoint = "1"
                R.id.taste_2 -> tastePoint = "2"
                R.id.taste_3 -> tastePoint = "3"
                R.id.taste_4 -> tastePoint = "4"
                R.id.taste_5 -> tastePoint = "5"
            }
            Log.d(TAG, "tastePoint : $tastePoint ")
        }
        beauty.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.beauty_1 -> beautyPoint = "1"
                R.id.beauty_2 -> beautyPoint = "2"
                R.id.beauty_3 -> beautyPoint = "3"
                R.id.beauty_4 -> beautyPoint = "4"
                R.id.beauty_5 -> beautyPoint = "5"
            }
            Log.d(TAG, "beautyPoint : $beautyPoint ")
        }
        study.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.study_1 -> studyPoint = "1"
                R.id.study_2 -> studyPoint = "2"
                R.id.study_3 -> studyPoint = "3"
                R.id.study_4 -> studyPoint = "4"
                R.id.study_5 -> studyPoint = "5"
            }
            Log.d(TAG, "studyPoint : $studyPoint ")
        }
        submitReview.setOnClickListener {
            totalReview = editReview.text.toString()
            dbModel.createReview(totalReview,tastePoint,beautyPoint,studyPoint,position)
            finish()


        }



    }
}