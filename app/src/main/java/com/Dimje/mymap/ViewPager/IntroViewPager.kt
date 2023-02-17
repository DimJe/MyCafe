package com.Dimje.mymap.ViewPager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.Dimje.mymap.UI.activity.MainActivity
import com.Dimje.mymap.R
import com.Dimje.mymap.databinding.ActivityIntroViewPagerBinding
import java.util.*

class IntroViewPager : AppCompatActivity() {

    companion object{
        const val TAG:String = "로그"
    }
    private var pageItemList = ArrayList<PageItem>()
    private lateinit var introPagerRecyclerAdapter: IntroViewPagerAdapter
    private val binding : ActivityIntroViewPagerBinding by lazy {
        ActivityIntroViewPagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        pageItemList.add(PageItem(R.color.colorOrange,R.drawable.intro_image1,"밥도 먹었겠다\n 커피 한잔 할까??"))
        pageItemList.add(PageItem(R.color.colorBlue,R.drawable.intro_image2,"여기 주변엔 무슨 카페가 있지??"))
        pageItemList.add(PageItem(R.color.colorWhite,R.drawable.intro_image3,"여기 평가 좋네\n 여기로 가자"))

        binding.previousBtn.setOnClickListener {
            binding.myIntroViewPager.currentItem -= 1
            binding.nextBtn.text = "다음"
        }
        binding.nextBtn.setOnClickListener {
            if(binding.myIntroViewPager.currentItem==2){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                binding.myIntroViewPager.currentItem += 1
                if(binding.myIntroViewPager.currentItem==2){
                    binding.nextBtn.text = "확인"
                }
                else binding.nextBtn.text = "다음"
            }
        }
        introPagerRecyclerAdapter = IntroViewPagerAdapter(pageItemList)


        binding.myIntroViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                if (position==2) binding.nextBtn.text = "확인"
                else binding.nextBtn.text = "다음"
                super.onPageSelected(position)
            }
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_DRAGGING && binding.myIntroViewPager.currentItem==2){
                    val intent = Intent(this@IntroViewPager, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                super.onPageScrollStateChanged(state)
            }
        })
        binding.myIntroViewPager.adapter = introPagerRecyclerAdapter
        binding.myIntroViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.dotsIndicator.setViewPager2(binding.myIntroViewPager)
    }
}