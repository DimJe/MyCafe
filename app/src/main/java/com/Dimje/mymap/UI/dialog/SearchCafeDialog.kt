package com.Dimje.mymap.UI.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.fragment.app.DialogFragment
import com.Dimje.mymap.R
import com.Dimje.mymap.databinding.DialogCafeSearchBinding


class SearchCafeDialog(dialogListener: DialogListener,id:Int) : DialogFragment() {
    private var _binding: DialogCafeSearchBinding? = null
    private val binding get() = _binding!!

    private var dialogListener: DialogListener? = null
    private var id: Int? = null
    private var type = "none"
    private var selected: Button? = null

    init {
        this.id = id
        this.dialogListener = dialogListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCafeSearchBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = binding.root

        binding.searchBrand.forEach { view ->
            view.setOnClickListener {
                type = (it as Button).text.toString()
                if(selected==null){
                    (it as Button).isSelected = true
                    selected = it as Button
                }
                else{
                    if(selected==it as Button){
                        selected!!.isSelected = false
                        type = "none"
                        selected = null
                    }
                    else{
                        selected!!.isSelected = false
                        type = (it as Button).text.toString()
                        selected = it
                        selected!!.isSelected = true
                    }
                }
            }
        }
        binding.submit.setOnClickListener {
            if(type == "none"){
                Toast.makeText(requireActivity(), "하나를 선택해주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                this.dialogListener?.onSearchClick(id!!,type)
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