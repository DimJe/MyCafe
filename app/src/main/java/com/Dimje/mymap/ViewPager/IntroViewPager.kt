package com.Dimje.mymap.ViewPager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.Dimje.mymap.MainActivity
import com.Dimje.mymap.R
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.android.synthetic.main.activity_intro_view_pager.*
import kotlinx.android.synthetic.main.activity_intro_view_pager.view.*
import java.util.*
import javax.security.auth.callback.Callback

class IntroViewPager : AppCompatActivity() {

    companion object{
        const val TAG:String = "로그"
    }
    private var pageItemList = ArrayList<PageItem>()
    private lateinit var IntroPagerRecyclerAdapter: IntroViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_view_pager)
        pageItemList.add(PageItem(R.color.colorOrange,R.drawable.intro_image1,"밥도 먹었겠다\n 커피 한잔 할까??"))
        pageItemList.add(PageItem(R.color.colorBlue,R.drawable.intro_image2,"여기 주변엔 무슨 카페가 있지??"))
        pageItemList.add(PageItem(R.color.colorWhite,R.drawable.intro_image3,"여기 평가 좋네\n 여기로 가자"))

        previous_btn.setOnClickListener {
            my_intro_view_pager.currentItem -= 1
            next_btn.text = "다음"
        }
        next_btn.setOnClickListener {
            if(my_intro_view_pager.currentItem==2){
                Log.d(TAG, "currentItem is 2")
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Log.d(TAG, "${my_intro_view_pager.currentItem}")
                my_intro_view_pager.currentItem += 1
                if(my_intro_view_pager.currentItem==2){
                    next_btn.text = "확인"
                }
                else next_btn.text = "다음"
            }
        }
        Log.d(TAG, "intro: called")
        IntroPagerRecyclerAdapter = IntroViewPagerAdapter(pageItemList)


        my_intro_view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                if (position==2) next_btn.text = "확인"
                else next_btn.text = "다음"
                super.onPageSelected(position)
            }
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_DRAGGING && my_intro_view_pager.currentItem==2){
                    val intent = Intent(this@IntroViewPager,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                super.onPageScrollStateChanged(state)
            }
        })
        my_intro_view_pager.adapter = IntroPagerRecyclerAdapter
        my_intro_view_pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        dots_indicator.setViewPager2(my_intro_view_pager)
    }
}