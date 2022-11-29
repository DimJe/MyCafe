package com.Dimje.mymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.RadioButton
import androidx.core.view.isVisible
import com.Dimje.mymap.MainActivity.Companion.model
import com.Dimje.mymap.databinding.ActivityCafeBrandCheckBoxBinding

class CafeBrandCheckBox : AppCompatActivity() {
    var name : String? = null
    private val binding : ActivityCafeBrandCheckBoxBinding by lazy {
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        ActivityCafeBrandCheckBoxBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(MainActivity.TAG, "checkbox called ")


        binding.sumit.setOnClickListener {
            Log.d(MainActivity.TAG, "sumit : clicked ")
            when(name){
                "all" ->{
                    Log.d(MainActivity.TAG, " all ")
                    model.loadCafe_other()
                }
                null ->{
                    Log.d(MainActivity.TAG, " null ")
                    model.loadCafe(binding.writeCafe.text.toString())
                }
                else ->{
                    Log.d(MainActivity.TAG, " else")
                    model.loadCafe(name!!)
                }
            }
            finish()
        }

    }
    fun onRadioButtonClicked(view: View) {
        Log.d(MainActivity.TAG, "onRadioButtonClicked: called")
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
                R.id.edit ->
                    if (checked) {
                        name = null
                        binding.writeCafe.isVisible = true
                    }
            }
        }
    }
}