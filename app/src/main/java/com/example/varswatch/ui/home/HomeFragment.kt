package com.example.varswatch.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.varswatch.*
import com.example.varswatch.databinding.FragmentHomeBinding
import com.example.varswatch.domain.model.SearchResults.Item
import com.example.varswatch.ui.epoxy_controller.VideosEpoxyController
import com.example.varswatch.util.SharedData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), VideosEpoxyController.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getSubscribedChannels()

        viewModel.videoInfo.observe(viewLifecycleOwner) {
            initialize(it)
        }

        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    sumbit(query)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        return root
    }

    private fun sumbit(query: String) {
        SharedData.query = query
        val controller = findNavController()
        controller.navigate(R.id.searchFragment)
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