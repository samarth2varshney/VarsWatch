package com.example.varswatch.ui.play_list

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.varswatch.R
import com.example.varswatch.databinding.FragmentPlayListBinding
import com.example.varswatch.ui.epoxy_controller.PlayListsController
import com.example.varswatch.util.VarsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayListFragment : VarsFragment(),PlayListsController.OnItemClickListener{

    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListBinding.inflate(inflater, container, false)

        viewModel.getPlayLists()

        viewModel.listOfPlayLists.observe(viewLifecycleOwner) {
            initialize(it)
        }

        return binding.root
    }

    private fun initialize(videoInfo: List<String>){
        val controller = PlayListsController(this)
        if(videoInfo.isEmpty()){
            binding.playListText.visibility = View.VISIBLE
        }else{
            binding.playListText.visibility = View.GONE
        }
        binding.apply {
            epoxyRecyclerView.setController(controller)
            controller.setData(videoInfo)
            progressBar.visibility = View.GONE
        }

    }

    override fun onItemClick(item: String) {
        val bundle = PlayListVideosFragment.bundle(item)
        findNavController().navigate(R.id.playListVideosFragment, bundle)
    }

}