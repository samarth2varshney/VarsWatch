package com.example.varswatch.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.varswatch.data.SearchResultsDto
import com.example.varswatch.databinding.FragmentSearchBinding
import com.example.varswatch.util.SharedData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root = binding.root

        viewModel.search(SharedData.query)

        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->

            binding.searchResultRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            val adapter = search_video_adapter(requireContext(), searchResults)
            binding.searchResultRecyclerView.adapter = adapter

        }

        return root
    }

}