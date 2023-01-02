package com.aedo.my_heaven.util.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.aedo.my_heaven.databinding.CustomDialogBinding
import com.aedo.my_heaven.util.activity.setOnDebounceClickListener

class CustomDialogFragment : DialogFragment() {

    private var _binding: CustomDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = arguments?.getString("titleContext")
        binding.body.text = arguments?.getString("bodyContext")

        val btnBundle = arguments?.getStringArray("btnData")

        binding.btn.setOnDebounceClickListener {
            buttonClickListener.onClickYes()
            dismiss()
        }
        binding.btn.text = btnBundle?.get(1)

        if (btnBundle?.size == 1) {
            binding.btnCancel.visibility = View.GONE

        } else {
            binding.btnCancel.setOnDebounceClickListener {
                buttonClickListener.onClickNo()
                dismiss()
            }
            binding.btnCancel.text = btnBundle?.get(0)
        }
    }

    interface OnButtonClickListener {
        fun onClickYes()
        fun onClickNo()
    }

    override fun onStart() {
        super.onStart();
        val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
        lp.copyFrom(dialog!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        val window: Window = dialog!!.window!!
        window.attributes = lp
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }

    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener
}