package com.Dimje.mymap.UI.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.Dimje.mymap.R
import com.Dimje.mymap.databinding.DialogAddReviewBinding
import java.util.*

class AddReviewDialog(dialogListener: DialogListener,id:Int) : DialogFragment() {
    private var _binding: DialogAddReviewBinding? = null
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
        _binding = DialogAddReviewBinding.inflate(inflater,container,false)
        val view = binding.root
        var point = 0.0f
        binding.point.setOnCheckedChangeListener { _, id ->
            point = when(id){
                R.id.point_1 -> 1.0f
                R.id.point_2 -> 2.0f
                R.id.point_3 -> 3.0f
                R.id.point_4 -> 4.0f
                R.id.point_5 -> 5.0f
                else -> {0.0f}
            }
        }

        binding.submitReview.setOnClickListener {
            if(point==0.0f || binding.editReview.text.isNullOrEmpty()){
                Toast.makeText(requireActivity(), "올바른 입력을 해주세요", Toast.LENGTH_SHORT).show()
            }
            else{
                this.dialogListener?.onSubmitClick(id!!,binding.editReview.text.toString(),point)
                dismiss()
            }
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}