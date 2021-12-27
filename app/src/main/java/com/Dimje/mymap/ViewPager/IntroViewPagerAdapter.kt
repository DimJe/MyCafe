package com.Dimje.mymap.ViewPager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Dimje.mymap.R

class IntroViewPagerAdapter(private var pageList:ArrayList<PageItem>)
    : RecyclerView.Adapter<IntroViewPagerHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewPagerHolder {
        return IntroViewPagerHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.layout_intro_pager_item,parent,false))
    }

    override fun onBindViewHolder(holder: IntroViewPagerHolder, position: Int) {
        holder.bindWithView(pageList[position])
    }

    override fun getItemCount(): Int {
        return pageList.size
    }

}