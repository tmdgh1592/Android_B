package com.umc.clone_flo.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umc.clone_flo.databinding.LayoutBottomSheetBinding

class BottomSheetFragment: BottomSheetDialogFragment() {
    interface OnItemClickListener {
        fun onClick(position: Int)
    }
    interface OnDismissListener {
        fun onDismiss()
    }

    private lateinit var binding: LayoutBottomSheetBinding
    private lateinit var clickListener: OnItemClickListener
    private lateinit var dismissListener: OnDismissListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.unlikeBtn.setOnClickListener {
            clickListener.onClick(3)
            dismiss()
        }
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }
    fun setOnDismissListener(listener: OnDismissListener) {
        this.dismissListener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener.onDismiss()
    }
}