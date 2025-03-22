package com.example.varswatch.ui.play_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import com.example.varswatch.databinding.FragmentPlayListBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayListBottomFragment(listener: CardDialogListener)  : BottomSheetDialogFragment(){

    private val viewModel: PlayListViewModel by viewModels()
    private var _binding: FragmentPlayListBottomBinding? = null
    private val binding get() = _binding!!
    var playListName:String = ""

    private var mBottomSheetListener: CardDialogListener?=null

    init {
        this.mBottomSheetListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /** attach listener from parent fragment */
        try {
            mBottomSheetListener = context as CardDialogListener?
        }
        catch (e: ClassCastException){
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListBottomBinding.inflate(inflater, container, false)

        viewModel.getPlayLists()

        viewModel.listOfPlayLists.observe(viewLifecycleOwner){
            initialize(it)
        }

        binding.newPlaylistButton.setOnClickListener {
            viewModel.addNewPlayList(binding.textView.text.toString())
        }

        binding.button.setOnClickListener {
            if(playListName!=""){
                mBottomSheetListener?.close(playListName)
            }
        }

        return binding.root
    }

    private fun initialize(videoInfo: List<String>) {

        binding.apply {

            radioGroup.removeAllViews() // Remove existing radio buttons

            for ((index,text) in videoInfo.withIndex()) {
                val radioButton = RadioButton(requireContext()).apply {
                    layoutParams = RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                    )
                    setPadding(4, 4, 4, 4)
                    textSize = 20f
                    id = View.generateViewId() // Ensure a unique ID
                    isChecked = false
                }
                radioButton.text = text
                radioGroup.addView(radioButton)
            }

            // Optional: Set default selection if list is not empty
            if (videoInfo.isNotEmpty()) {
                radioGroup.check(radioGroup.getChildAt(0).id)
                playListName = videoInfo[0]
            }

            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val selectedRadioButton = binding.radioGroup.findViewById<RadioButton>(checkedId)
                playListName = selectedRadioButton?.text.toString()
            }

        }

    }

    interface CardDialogListener {
        fun close(playListName:String)
    }

}