package com.example.varswatch.ui.play_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.varswatch.MainActivity
import com.example.varswatch.R
import com.example.varswatch.databinding.FragmentPlayListVideosBinding
import com.example.varswatch.domain.model.SearchResults
import com.example.varswatch.domain.model.SearchResults.Item
import com.example.varswatch.ui.epoxy_controller.VideosEpoxyController
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "playList"

@AndroidEntryPoint
class PlayListVideosFragment : Fragment(), VideosEpoxyController.OnItemClickListener  {
    private var param: String? = null

    private var _binding: FragmentPlayListVideosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListVideosBinding.inflate(inflater, container, false)

        viewModel.getPlayList(param!!)

        viewModel.videoInfo.observe(viewLifecycleOwner) {
            initialize(it)
        }

        return binding.root
    }

    private fun initialize(videoInfo: List<Item>) {
        val controller = VideosEpoxyController(this)

        binding.apply {
            epoxyRecyclerView.setController(controller)
            controller.setData(videoInfo)
            progressBar.visibility = View.GONE
        }

    }

    companion object {
        fun bundle(param1: String) =
         Bundle().apply {
            putString(ARG_PARAM1, param1)
         }
    }

    override fun onItemClick(item: Item) {
        (activity as? MainActivity)?.openPlayer(item)
    }
}