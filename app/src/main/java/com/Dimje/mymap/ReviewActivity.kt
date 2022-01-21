package com.Dimje.mymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.Dimje.mymap.MainActivity.Companion.TAG

class ReviewActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        Log.d(TAG, "ReviewActivity - onCreate : called ")
        supportFragmentManager.beginTransaction()
                                    .add(R.id.review,NoReview())
                                    .commit()
    }
}