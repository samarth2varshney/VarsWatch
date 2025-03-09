package com.example.varswatch.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.varswatch.MainActivity
import com.example.varswatch.databinding.FragmentSearchBinding
import com.example.varswatch.domain.model.SearchResults
import com.example.varswatch.ui.epoxy_controller.VideosChannelsEpoxyController
import com.example.varswatch.ui.play_list.PlayListBottomFragment
import com.example.varswatch.util.SharedData
import com.example.varswatch.util.VarsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : VarsFragment(), VideosChannelsEpoxyController.OnItemClickListener,PlayListBottomFragment.CardDialogListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    lateinit var dialog: PlayListBottomFragment /** BottomSheetDialogFragment for choose card */
    lateinit var listener: PlayListBottomFragment.CardDialogListener /** Listener for choose card */

    var item: SearchResults.Item?=null

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root = binding.root

        listener = this

        val controller = VideosChannelsEpoxyController(this)

        binding.apply {

            epoxyRecyclerView.setController(controller)

            viewModel.search(SharedData.query)

            progressBar.visibility = View.VISIBLE

            viewModel.videoInfo.observe(viewLifecycleOwner) {
                controller.setData(it)
                progressBar.visibility = View.GONE
            }

        }

        return root
    }

    override fun onItemClick(item: SearchResults.Item, subscribe: Boolean, saveToPlayList: Boolean) {
        if(saveToPlayList){
            this.item = item
            showBottomSheetDialogFragment()
        }
        else if(subscribe)
            viewModel.subscribeToChannel(item)
        else if(item.id.videoId!="") {
            (activity as? MainActivity)?.openPlayer(item)
        }
    }

    private fun showBottomSheetDialogFragment() {
        dialog = PlayListBottomFragment(listener)
        dialog.show(parentFragmentManager, "tag")
    }

    override fun close(playListName:String) {
        viewModel.saveToPlayList(playListName,item!!)
        dialog.dismiss()
    }

}