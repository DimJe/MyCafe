package com.Dimje.mymap.ViewPager

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Dimje.mymap.R

class IntroViewPagerHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

    private val itemImage = itemView.findViewById<ImageView>(R.id.pager_item_image)
    private val itemContent = itemView.findViewById<TextView>(R.id.pager_item_text)
    private val itemBg = itemView.findViewById<LinearLayout>(R.id.pager_item_bg)

    fun bindWithView(pageItem: PageItem){
        itemImage.setImageResource(pageItem.imageSrc)
        itemBg.setBackgroundResource(pageItem.bgColor)
        itemContent.text = pageItem.content
    }
}