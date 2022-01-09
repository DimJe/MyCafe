package com.Dimje.mymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.RadioButton
import com.Dimje.mymap.API.CallAPI
import kotlinx.android.synthetic.main.activity_cafe_brand_check_box.*

class CafeBrandCheckBox : AppCompatActivity() {
    var name : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_cafe_brand_check_box)
        Log.d(MainActivity.TAG, "checkbox called ")

        sumit.setOnClickListener {
            when(name){
                "all" ->{
                    CafeMapActivity.callAPI.loadCafe_other()
                }
                null ->{
                    CafeMapActivity.callAPI.loadCafe(edit.text.toString())
                }
                else ->{
                    CafeMapActivity.callAPI.loadCafe(name!!)
                }
            }
            finish()
        }

    }
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.ediya ->
                    if (checked) {
                        name = "이디야"
                    }
                R.id.two ->
                    if (checked) {
                        name = "투썸"
                    }
                R.id.star ->
                    if (checked) {
                        name = "스타벅스"
                    }
                R.id.mega ->
                    if (checked) {
                        name = "메가커피"
                    }
                R.id.all ->
                    if (checked) {
                        name = "all"
                    }
            }
        }
    }
}