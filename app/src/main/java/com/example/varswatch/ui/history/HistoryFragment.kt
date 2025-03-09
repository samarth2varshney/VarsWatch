package com.example.varswatch.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.varswatch.MainActivity
import com.example.varswatch.databinding.FragmentHistoryBinding
import com.example.varswatch.domain.model.SearchResults.Item
import com.example.varswatch.ui.epoxy_controller.VideosEpoxyController
import com.example.varswatch.util.VarsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : VarsFragment(), VideosEpoxyController.OnItemClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

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

    override fun onItemClick(item: Item) {
        if(item.id.videoId!="") {
            (activity as? MainActivity)?.openPlayer(item)
        }
    }
}