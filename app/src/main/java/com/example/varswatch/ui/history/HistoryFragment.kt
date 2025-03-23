package com.example.varswatch.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.varswatch.MainActivity
import com.example.varswatch.databinding.FragmentHistoryBinding
import com.example.varswatch.domain.model.SearchResults
import com.example.varswatch.domain.model.SearchResults.Item
import com.example.varswatch.ui.epoxy_controller.VideosEpoxyController
import com.example.varswatch.ui.play_list.PlayListBottomFragment
import com.example.varswatch.ui.search.SearchViewModel
import com.example.varswatch.util.VarsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : VarsFragment(), VideosEpoxyController.OnItemClickListener,
    PlayListBottomFragment.CardDialogListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var dialog: PlayListBottomFragment /** BottomSheetDialogFragment for choose card */
    private lateinit var listener: PlayListBottomFragment.CardDialogListener /** Listener for choose card */

    var item: Item?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        listener = this

        viewModel.getHistory()

        binding.apply {
            progressBar.visibility = View.VISIBLE
            viewModel.videoInfo.observe(viewLifecycleOwner) {
                initialize(it)
            }
        }

        return root
    }

    private fun initialize(videoInfo: List<Item>) {
        val controller = VideosEpoxyController(this)

        if(videoInfo.isEmpty()){
            binding.subscribeText.visibility = View.VISIBLE
        }else{
            binding.subscribeText.visibility = View.GONE
        }

        binding.apply {
            epoxyRecyclerView.setController(controller)
            controller.setData(videoInfo)
            progressBar.visibility = View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(item: Item, saveToPlayList: Boolean) {
        if(saveToPlayList){
            this.item = item
            showBottomSheetDialogFragment()
        }
        else if(item.id.videoId!="") {
            (activity as? MainActivity)?.openPlayer(item)
        }
    }

    private fun showBottomSheetDialogFragment() {
        dialog = PlayListBottomFragment(listener)
        dialog.show(parentFragmentManager, "tag")
    }

    override fun close(playListName: String) {
        searchViewModel.saveToPlayList(playListName,item!!)
        dialog.dismiss()
    }
}