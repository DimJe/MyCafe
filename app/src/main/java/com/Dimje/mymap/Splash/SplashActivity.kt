package com.Dimje.mymap.Splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.Dimje.mymap.MainActivity
import com.Dimje.mymap.ViewPager.IntroViewPager


class SplashActivity : AppCompatActivity() {
    companion object{
        const val TAG:String = "로그"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "splash: called")
        val intent = Intent(this, IntroViewPager::class.java)
        startActivity(intent)
        finish()
    }
}