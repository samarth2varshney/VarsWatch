package com.example.varswatch.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.varswatch.VideoPlayerActivity
import com.example.varswatch.data.SearchResultsDto
import com.example.varswatch.databinding.FragmentSearchBinding
import com.example.varswatch.util.SharedData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchEpoxyController.OnItemClickListener {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.apply {

            viewModel.search(SharedData.query)

            viewModel.videoInfo.observe(viewLifecycleOwner) { it ->

                val controller = SearchEpoxyController(this@SearchFragment)
                epoxyRecyclerView.setController(controller)
                controller.setData(it)

            }

        }

        return root
    }

    override fun onItemClick(item: SearchResultsDto.Item) {
        val intent = Intent(requireContext(), VideoPlayerActivity::class.java)
        intent.putExtra("youtubelink",item.id.videoId)
        intent.putExtra("youtubetitle",item.snippet.title)
        startActivity(intent)
    }

}