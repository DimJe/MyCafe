package com.Dimje.mymap.UI.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import com.Dimje.mymap.R
import com.Dimje.mymap.databinding.DialogMinigameBinding
import java.util.*

class MiniGameDialog(dialogListener: DialogListener,id:Int) : DialogFragment() {
    private var _binding: DialogMinigameBinding? = null
    private val binding get() = _binding!!

    private var dialogListener: DialogListener? = null
    private var id: Int? = null

    init {
        this.id = id
        this.dialogListener = dialogListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogMinigameBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = binding.root

        val random = Random().nextInt(11) + 1


        binding.grid.children.forEach {
            val anim = AnimationUtils.loadAnimation(requireContext(),R.anim.slide_down)
            it.setOnClickListener { textView ->

                textView.startAnimation(anim)
                if(textView.tag.toString().toInt() == random){
                    (textView as TextView).text = "당첨"
                    binding.result.text = "기분 좋게 커피를 사주세요"
                }
                else{
                    (textView as TextView).text = "꽝"
                }
            }
        }
        binding.confirm.setOnClickListener {
            dismiss()
        }


        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}